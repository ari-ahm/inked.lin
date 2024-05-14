package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpExchange;
import ir.doodmood.mashtframework.annotation.http.*;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class MashtDTO {
    @Getter
    private final HttpExchange httpExchange;
    private final LinkedList<String> pathVariables = new LinkedList<>();
    public MashtDTO(HttpExchange httpContext) {
        this.httpExchange = httpContext;
    }

    public void pushPathVariable(String s) {
        pathVariables.addLast(s);
    }

    public void popPathVariable() {
        pathVariables.removeLast();
    }

    public LinkedList<String> getPathVariables() {
        return new LinkedList<>(pathVariables);
    }

    public LinkedList<String> getLinkList() {
        LinkedList<String> ret = new LinkedList<>();
        for (String s : Arrays.stream(httpExchange.getRequestURI().getPath().split("/")).toList()) {
            if (s.isEmpty() || s.isBlank()) continue;
            ret.addLast(s);
        }
        return ret;
    }

    public Class getRequestType() {
        return switch (httpExchange.getRequestMethod()) {
            case "GET" -> GetMapping.class;
            case "POST" -> PostMapping.class;
            case "PUT" -> PutMapping.class;
            case "DELETE" -> DeleteMapping.class;
            case "PATCH" -> PatchMapping.class;
            default -> null;
        };
    }

    @Deprecated
    public void send404() throws IOException { // TODO: handle it cleaner. it's just a temp thing to get it working.
        byte[] ret = "NOT FOUND".getBytes();
        httpExchange.sendResponseHeaders(404, ret.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(ret);
        os.close();
        httpExchange.close();
    }
}
