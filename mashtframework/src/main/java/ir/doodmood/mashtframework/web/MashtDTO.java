package ir.doodmood.mashtframework.web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ir.doodmood.mashtframework.annotation.http.*;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.JWTTokenExpiredException;
import ir.doodmood.mashtframework.exception.JWTVerificationFailedException;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class MashtDTO {
    private final HttpExchange httpExchange;
    private final LinkedList<String> pathVariables = new LinkedList<>();
    @Setter
    private int responseCode = 200;
    private HashMap<String, String> cookies;

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

    public void setCookie(String payload, long lifetime, String path, String cookieName, String domain) {
        String ageAttrib = "";
        if (lifetime > 0) ageAttrib = "; Max-Age=" + lifetime;
        String domainAttrib = "";
        if (domain != null) domainAttrib = "; Domain=" + domain;
        String pathAttrib = "";
        if (path != null) pathAttrib = "; Path=" + path;
        httpExchange.getResponseHeaders().add("Set-Cookie", cookieName + "=" + payload + "; Secure" + ageAttrib + pathAttrib + domainAttrib);
    }

    public void setJWTToken(Object payload, long lifetime, String path, String cookieName, String domain) {
        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();
        String csrf = jwt.generateCSRFToken(32);
        setCookie(jwt.getToken(payload, lifetime, csrf), lifetime, path, cookieName, domain);
        httpExchange.getResponseHeaders().add("X-CSRF-Token-" + cookieName, csrf);
    }

    public void setJWTToken(Object payload, long lifetime) {
        setJWTToken(payload, lifetime, "/", "JWT", null);
    }

    public void setJWTToken(Object payload) {
        setJWTToken(payload, -1, "/", "JWT", null);
    }

    public Object readJWTToken(Class clazz, String cookieName)
            throws NoSuchElementException,
            JWTTokenExpiredException,
            JWTVerificationFailedException {
        if (cookies == null) readCookies();
        if (!cookies.containsKey(cookieName)) {
            ((Logger) ComponentFactory.factory(Logger.class).getNew()).info("Couldn't find a cookie named", cookieName);
            throw new NoSuchElementException();
        }

        JWT jwt = (JWT) ComponentFactory.factory(JWT.class).getNew();

        String csrf = null;
        if (httpExchange.getRequestHeaders().containsKey("X-CSRF-Token-" + cookieName)
            && !httpExchange.getRequestHeaders().get("X-CSRF-Token-" + cookieName).isEmpty())
            csrf = httpExchange.getRequestHeaders().get("X-CSRF-Token-" + cookieName).get(0);

        return jwt.getPayload(clazz, cookies.get(cookieName), csrf);
    }

    public Object readJWTToken(Class clazz)
            throws NoSuchElementException,
            JWTTokenExpiredException,
            JWTVerificationFailedException {
        return readJWTToken(clazz, "JWT");
    }

    public void readCookies() {
        cookies = new HashMap<>();
        List<String> cookiesList = httpExchange.getRequestHeaders().get("Cookie");
        if (cookiesList == null || cookiesList.isEmpty()) return;
        String[] cookieString = cookiesList.get(0).split(";");
        for (String cookie : cookieString) {
            cookies.put(cookie.substring(0, cookie.indexOf('=')).trim(), cookie.substring(cookie.indexOf('=') + 1).trim());
        }
    }

    public HashMap<String, String> getCookies() {
        if (cookies == null) readCookies();
        return new HashMap<>(cookies);
    }
}
