package ir.doodmood.mashtframework.web;

import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.*;
import ir.doodmood.mashtframework.exception.DuplicatePathAndMethodException;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.management.InstanceAlreadyExistsException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.Set;

public class MashtApplication {
    private final Server server;
    private final RequestHandler requestHandler;
    @Getter
    private static boolean isAlreadyUp;

    public static MashtApplication run(Class mainClass)
            throws DuplicatePathAndMethodException,
            InstanceAlreadyExistsException {
        if (isAlreadyUp)
            throw new InstanceAlreadyExistsException("an instance of MashtApplication is already up.");
        isAlreadyUp = true;
        return new MashtApplication(mainClass);
    }

    public void close() {
        server.close();
        isAlreadyUp = false;
    }

    private MashtApplication(Class mainClass)
            throws DuplicatePathAndMethodException {
        ComponentFactory.addClassWrapper(SessionFactory.class, (HasFactoryMethod) ComponentFactory.factory(HibernateSessionFactoryWrapper.class).getNew());

        ComponentFactory srvFactory = ComponentFactory.factory(Server.class);
        server = (Server)srvFactory.getNew();
        this.requestHandler = server.getRequestHandler();
        Reflections reflections = new Reflections(mainClass.getPackage().getName(), new SubTypesScanner(false));
        Set<Class<?>> packageClasses = reflections.getSubTypesOf(Object.class);
        resolveEndpoints(packageClasses);
        server.run();
    }

    private void resolveEndpoints(Set<Class<?>> packageClasses)
            throws DuplicatePathAndMethodException {
        Logger logger = (Logger) ComponentFactory.factory(Logger.class).getNew();


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

                    LinkedList<String> subPath = new LinkedList<>(path);

                    String annotationValue = "";
                    try {
                        annotationValue = (String) i.getMethod("value").invoke(m.getAnnotation(i));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        logger.error("Impossible error? ", e);
                    }


                    for (String j : annotationValue.split("/")) {
                        if (!j.isEmpty() && !j.isBlank()) subPath.add(j);
                    }

                    logger.info("disovered path : ", subPath, i.getName());

                    requestHandler.addPath(subPath, m, i);
                }
            }
        }
    }
}
