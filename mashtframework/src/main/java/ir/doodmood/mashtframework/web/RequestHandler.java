package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.annotation.http.*;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

@Component
class RequestHandler implements HttpHandler {
    private final Class[] endpointAnnotations = {
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            PatchMapping.class
    };

    private final HashMap<String, Method> methods = new HashMap<>();
    private final HashMap<String, RequestHandler> routes = new HashMap<>();
    private final ControllerContainer controllerContainer;

    @Autowired
    private RequestHandler(ControllerContainer controllerContainer) {
        this.controllerContainer = controllerContainer;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        MashtDTO context = new MashtDTO((HttpsExchange) httpExchange);
        runPath(context, context.getLinkList());
    }

    public void addPath(LinkedList<String> path, Method endpoint) throws DuplicatePathAndMethodException {
        if (path == null || path.isEmpty()) {
            for (Class i : endpointAnnotations) {
                if (!endpoint.isAnnotationPresent(i)) continue;

                if (methods.containsKey(i.getName()))
                    throw new DuplicatePathAndMethodException(String.format("In method : %s", endpoint.getName()));

                methods.put(i.getName(), endpoint);
            }

            return;
        }

        if (routes.containsKey(path.getFirst())) {
            path.removeFirst();
            routes.get(path.getFirst()).addPath(path, endpoint);
        } else {
            RequestHandler newPath = new RequestHandler(controllerContainer);
            routes.put(path.getFirst(), newPath);
            path.removeFirst();
            newPath.addPath(path, endpoint);
        }
    }

    public boolean runPath(MashtDTO dto, LinkedList<String> path) {
        if (path == null || path.isEmpty()) {
            if (!methods.containsKey(dto.getRequestType().getName()))
                return false;

            methods.get(dto.getRequestType().getName()).invoke(
                    ControllerContainer.getObject(methods.get(dto.getRequestType().getName()).getDeclaringClass()),
                    dto);
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

        dto.send404();
        return false;
    }
}
