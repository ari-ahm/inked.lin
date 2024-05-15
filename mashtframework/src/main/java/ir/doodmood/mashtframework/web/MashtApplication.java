package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.ComponentFactory;
import ir.doodmood.mashtframework.core.Config;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;

import javax.management.InstanceAlreadyExistsException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class MashtApplication {
    private final Server server;
    private final RequestHandler requestHandler;
    private static boolean isAlreadyUp;

    public static MashtApplication run(Class mainClass)
            throws DuplicatePathAndMethodException,
            InstanceAlreadyExistsException {
        if (isAlreadyUp)
            throw new InstanceAlreadyExistsException("an instance of MashtApplication is already up.");
        isAlreadyUp = true;
        return new MashtApplication(mainClass);
    }

    private MashtApplication(Class mainClass)
            throws DuplicatePathAndMethodException {
        ComponentFactory srvFactory = ComponentFactory.factory(Server.class);
        server = (Server)srvFactory.getNew();
        this.requestHandler = server.getRequestHandler();
        Set<Class> packageClasses = findAllClassesInPackage(mainClass.getPackage().getName(), getResource(mainClass.getPackage().getName().replace(".", "/")));
        resolveEndpoints(packageClasses);
        server.run();
    }

    private File getResource(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();
            logger.error("Impossible error? ", e);
            return null;
        }
    }

    private Set<Class> findAllClassesInPackage(String packageName, File dir) {
        Set<Class> ret = new HashSet<>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                ret.addAll(findAllClassesInPackage(packageName + "." + file.getName(), file));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                try {
                    ret.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().lastIndexOf("."))));
                } catch (ClassNotFoundException e) {
                    Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();
                    logger.error("Impossible error? ", e);
                }
            }
        }

        return ret;
    }

    private void resolveEndpoints(Set<Class> packageClasses)
            throws DuplicatePathAndMethodException {
        Boolean singletonControllers = (Boolean) ((Config) ComponentFactory.factory(Config.class).getNew())
                .get("singleton_controllers", Boolean.class);
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

                    Parameter[] parameters = m.getParameters();
                    if (parameters.length != 1 || !parameters[0].getType().equals(MashtDTO.class)) {
                        Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();
                        logger.error("Method ", m.getName(), " doesn't have the required signature: void func(MashtDTO). skipping");
                        continue;
                    }

                    LinkedList<String> subPath = new LinkedList<>(path);

                    String annotationValue = "";
                    try {
                        annotationValue = (String) i.getMethod("value").invoke(m.getAnnotation(i));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();
                        logger.error("Impossible error? ", e);
                    }


                    for (String j : annotationValue.split("/")) {
                        if (!j.isEmpty() && !j.isBlank()) subPath.add(j);
                    }

                    requestHandler.addPath(subPath, m, i);
                }
            }
        }
    }
}
