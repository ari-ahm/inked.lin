package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.CriticalError;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
class RequestHandler implements HttpHandler {
    private final HashMap<String, Method> methods = new HashMap<>();
    private final HashMap<String, List<Class>> methodsSingature = new HashMap<>();
    private final HashMap<String, RequestHandler> routes = new HashMap<>();
    private final Logger logger;

    @Autowired
    private RequestHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        MashtDTO context = new MashtDTO(httpExchange);

        // TODO: handle HEAD and other stuff...

        if (!runPath(context, context.getLinkList()))
            context.sendResponse(404, "Not Found");
    }

    void addPath(LinkedList<String> path, Method endpoint, Class method) throws DuplicatePathAndMethodException {
        if (path == null || path.isEmpty()) {

            endpoint.setAccessible(true);

            if (methods.containsKey(method.getName())) {
                logger.critical("Duplicate path in controller method: " + endpoint.getName());
                throw new DuplicatePathAndMethodException(String.format("In method : %s", endpoint.getName()));
            }

            methods.put(method.getName(), endpoint);
            List<Class> signList = new ArrayList<>();
            methodsSingature.put(method.getName(), signList);

            if (endpoint.getParameters().length != 1 || !endpoint.getParameters()[0].getType().equals(MashtDTO.class)) {
                logger.critical("Invalid requirements in method:" + endpoint.getDeclaringClass().getName(), endpoint.getName());
                throw new CriticalError();
            }

            return;
        }

        if (routes.containsKey(path.getFirst())) {
            String first = path.getFirst();
            path.removeFirst();
            routes.get(first).addPath(path, endpoint, method);
        } else {
            RequestHandler newPath = (RequestHandler) ComponentFactory.factory(RequestHandler.class).getNew();
            routes.put(path.getFirst(), newPath);
            path.removeFirst();
            newPath.addPath(path, endpoint, method);
        }
    }

    public boolean runPath(MashtDTO dto, LinkedList<String> path) {
        if (path == null || path.isEmpty()) {
            if (!methods.containsKey(dto.getRequestType().getName()))
                return false;

            try {
                methods.get(dto.getRequestType().getName()).invoke(
                        ComponentFactory.factory(methods.get(dto.getRequestType().getName()).getDeclaringClass()).getNew(),
                        dto);
            } catch (InvocationTargetException e) {
                logger.error("Invocation target exception: ", e);
                try {
                    dto.sendResponse(500, "Internal server error");
                } catch (IOException ioException) {
                    logger.error("IOException while sending back 500. something is wrong: ", ioException);
                }
            } catch (IllegalAccessException e) {
                logger.error("IllegalAccessException(shouldn't be happening): ", e);
            }
            return true;
        }

        String nextPath = path.getFirst();
        path.removeFirst();

        if (routes.containsKey(nextPath)) {
            boolean ret = routes.get(nextPath).runPath(dto, path);
            if (ret)
                return true;
        }
        if (routes.containsKey("{}")) {
            dto.pushPathVariable(nextPath);
            boolean ret = routes.get("{}").runPath(dto, path);
            if (ret)
                return true;
            dto.popPathVariable();
        }

        path.addFirst(nextPath);
        return false;
    }
}
