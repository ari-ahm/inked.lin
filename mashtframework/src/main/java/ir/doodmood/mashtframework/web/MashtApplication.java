package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Config;
import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;
import ir.doodmood.mashtframework.exception.IncorrectAnnotationException;

import javax.lang.model.element.AnnotationValue;
import javax.management.InstanceAlreadyExistsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: deal with the exceptions...

public class MashtApplication {
    private final Server server;
    private final RequestHandler requestHandler;
    private final Class mainClass;
    private static boolean isAlreadyUp;

    public static MashtApplication run(Class mainClass)
            throws IncorrectAnnotationException,
            CircularDependencyException,
            DuplicatePathAndMethodException,
            IOException,
            InstanceAlreadyExistsException {
        if (isAlreadyUp)
            throw new InstanceAlreadyExistsException("an instance of MashtApplication is already up.");
        isAlreadyUp = true;
        return new MashtApplication(mainClass);
    }

    private MashtApplication(Class mainClass)
            throws IncorrectAnnotationException,
            CircularDependencyException,
            DuplicatePathAndMethodException,
            IOException {
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

    private void resolveEndpoints(Set<Class> packageClasses)
            throws DuplicatePathAndMethodException,
            CircularDependencyException,
            IncorrectAnnotationException {
        Boolean singletonControllers = (Boolean) Config.getInstance().get(
                "singleton_controllers", Boolean.class);
        if (singletonControllers == null) singletonControllers = true;

        for (Class clazz : packageClasses) {
            if (!clazz.isAnnotationPresent(RestController.class)) continue;

            ComponentFactory.setSingleton(clazz, singletonControllers);

            LinkedList<String> path = new LinkedList<>();
            for (String i : ((RestController) clazz.getAnnotation(RestController.class)).value().split("/")) {
                if (!i.isEmpty() && !i.isBlank()) path.add(i);
            }

            for (Method m : clazz.getDeclaredMethods()) {
                for (Class i : RestController.endpointAnnotations) {
                    if (!m.isAnnotationPresent(i)) continue;

                    // TODO: check function signature

                    LinkedList<String> subPath = new LinkedList<>(path);

                    String annotationValue = "";
                    try {
                        annotationValue = (String) i.getMethod("value").invoke(m.getAnnotation(i));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        // TODO: fix this up.....
                        e.printStackTrace();
                    }


                    for (String j : annotationValue.split("/")) {
                        if (!j.isEmpty() && !j.isBlank()) subPath.add(j);
                    }

                    requestHandler.addPath(subPath, m);
                }
            }
        }
    }
}
