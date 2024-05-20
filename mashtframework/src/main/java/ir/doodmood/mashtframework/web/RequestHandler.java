package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

@Component
class RequestHandler implements HttpHandler {
    private final HashMap<String, Method> methods = new HashMap<>();
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

            if (methods.containsKey(method.getName()))
                throw new DuplicatePathAndMethodException(String.format("In method : %s", endpoint.getName()));

            methods.put(method.getName(), endpoint);

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
