package ir.doodmood.mashtframework.web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ir.doodmood.mashtframework.annotation.http.*;
import ir.doodmood.mashtframework.core.ComponentFactory;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public void setHttpResponseHeader(String key, String value) {
        httpExchange.getResponseHeaders().add(key, value);
    }

    public List<String> getHttpRequestHeader(String key) {
        return httpExchange.getResponseHeaders().get(key);
    }

    public void setJWTToken(Object payload, long lifetime) {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        String csrf = jwt.generateCSRFToken(32);
        httpExchange.getResponseHeaders().add("Set-Cookie", "JWT=" + jwt.getToken(payload, lifetime, csrf) + "; Secure");
        httpExchange.getResponseHeaders().add("X-CSRF-Token", csrf);
    }

    public Object readJWTToken(Class clazz) {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();

        return null;
    }
}
