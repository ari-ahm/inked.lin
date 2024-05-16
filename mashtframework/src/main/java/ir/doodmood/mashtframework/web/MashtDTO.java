package ir.doodmood.mashtframework.web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ir.doodmood.mashtframework.annotation.http.*;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class MashtDTO {
    private final HttpExchange httpExchange;
    private final LinkedList<String> pathVariables = new LinkedList<>();
    @Setter
    private int responseCode = 200;

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

    public void sendResponse(Object res) throws IOException {
        byte[] ret = new Gson().toJson(res).getBytes();
        httpExchange.sendResponseHeaders(responseCode, ret.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(ret);
        os.close();
        httpExchange.close();
    }

    public void sendResponse(int responseCode, Object res) throws IOException {
        byte[] ret = new Gson().toJson(res).getBytes();
        httpExchange.sendResponseHeaders(responseCode, ret.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(ret);
        os.close();
        httpExchange.close();
    }
}
