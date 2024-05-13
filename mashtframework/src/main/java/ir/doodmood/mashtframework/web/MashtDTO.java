package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpsExchange;
import lombok.Getter;

import java.util.LinkedList;

public class MashtDTO {
    @Getter
    private final HttpsExchange httpsContext;
    private final LinkedList<String> pathVariables = new LinkedList<>();
    public MashtDTO(HttpsExchange httpsContext) {
        this.httpsContext = httpsContext;
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
}
