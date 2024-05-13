package ir.doodmood.mashtframework.web;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsServer;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.annotation.Properties;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;

// TODO: VERY IMPORTANT: Handle IOException...

@Component
class Server {
    private final Integer listenPort;
    private final String appRootPath;
    @Getter
    private final RequestHandler requestHandler;

    @Autowired
    private Server(@Properties("listen_port") Integer listenPort,
                   @Properties("app_root_path") String appRootPath,
                   RequestHandler requestHandler) {
        if (listenPort == null) listenPort = 8080;
        if (appRootPath == null) appRootPath = "/";

        this.listenPort = listenPort;
        this.appRootPath = appRootPath;
        this.requestHandler = requestHandler;
    }

    public void run() throws IOException {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(listenPort),0);
        server.createContext(appRootPath, requestHandler);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
    }
}