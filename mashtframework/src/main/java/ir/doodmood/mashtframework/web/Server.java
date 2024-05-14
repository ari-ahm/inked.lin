package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpServer;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.annotation.Properties;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;

// TODO: VERY IMPORTANT: Handle IOException...
// TODO: handle https

@Component
class Server {
    private final Integer listenPort;
    private final String appRootPath;
    private final String listenHost;
    @Getter
    private final RequestHandler requestHandler;

    @Autowired
    private Server(@Properties("listen_port") Integer listenPort,
                   @Properties("listen_host") String listenHost,
                   @Properties("app_root_path") String appRootPath,
                   RequestHandler requestHandler) {
        if (listenPort == null) listenPort = 8080;
        if (listenHost == null) listenHost = "0.0.0.0";
        if (appRootPath == null) appRootPath = "/";

        this.listenPort = listenPort;
        this.appRootPath = appRootPath;
        this.requestHandler = requestHandler;
        this.listenHost = listenHost;
    }

    public void run() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(listenHost, listenPort),0);
        server.createContext(appRootPath, requestHandler);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
    }
}