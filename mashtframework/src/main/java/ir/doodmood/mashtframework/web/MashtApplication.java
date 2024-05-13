package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.IncorrectAnnotationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: deal with the exceptions...

public class MashtApplication {
    private final Server server;
    private final RequestHandler requestHandler;
    private final Class mainClass;

    public static MashtApplication run(Class mainClass) throws IncorrectAnnotationException, CircularDependencyException, IOException {
        return new MashtApplication(mainClass);
    }

    private MashtApplication(Class mainClass) throws IncorrectAnnotationException, CircularDependencyException, IOException {
        ComponentFactory srvFactory = ComponentFactory.factory(Server.class);
        server = (Server)srvFactory.getNew();
        this.mainClass = mainClass;
        this.requestHandler = server.getRequestHandler();
        Set<Class> packageClasses = findAllClassesInPackage(mainClass.getPackage().getName());
        resolveEndpoints(packageClasses);
        server.run();
    }

    private Set<Class> findAllClassesInPackage(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // TODO: handle the exception. is it even possible??
        }
        return null;
    }

    private void resolveEndpoints(Set<Class> packageClasses) {
        for (Class clazz : packageClasses) {
            if (!clazz.isAnnotationPresent(RestController.class)) continue;


        }
    }
}
