package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.CriticalError;
import ir.doodmood.mashtframework.exception.IncorrectAnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import ir.doodmood.mashtframework.annotation.*;

interface HasFactoryMethod {
    Object getNew();
}

@SuppressWarnings({"rawtypes", "unchecked"})
public class ComponentFactory implements HasFactoryMethod {
    private final ArrayList<HasFactoryMethod> dependencies = new ArrayList<>();
    private final Class persistentClass;
    private Constructor constructor;
    private static final HashMap<String, ComponentFactory> components = new HashMap<>();
    private boolean isResolving = false;
    private static final HashMap<String, Object> singletons = new HashMap<>();
    private final boolean singleton;

    private static class PropertiesComponent implements HasFactoryMethod {
        private final String key;
        private final Class persistentClass;

        public PropertiesComponent(String key, Class persistentClass) {
            this.key = key;
            this.persistentClass = persistentClass;
        }

        public Object getNew() {
            return ((Config) factory(Config.class).getNew()).get(key, persistentClass);
        }
    }

    private ComponentFactory(Class persistentClass) throws IncorrectAnnotationException, CircularDependencyException {
        this.persistentClass = persistentClass;

        boolean foundComponents = false, tmpSingleton = false;

        for (Annotation annotation : persistentClass.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Component.class)) {
                foundComponents = true;
                tmpSingleton |= annotation.annotationType().getAnnotation(Component.class).singleton();
            }
            if (annotation instanceof Component) {
                tmpSingleton |= ((Component) annotation).singleton();
            }
        }

        this.singleton = tmpSingleton;

        if (!foundComponents)
            throw new IncorrectAnnotationException(String.format("Class %s does not annotate Component", persistentClass.getName()));

        components.put(persistentClass.getName(), this);

        findConstructor();

        if (singleton)
            setSingleton(persistentClass, true);
    }

    public static ComponentFactory factory(Class persistentClass) {
        if (components.containsKey(persistentClass.getName()))
            return components.get(persistentClass.getName());

        try {
            return new ComponentFactory(persistentClass);
        } catch(CircularDependencyException | IncorrectAnnotationException e) {
            Logger logger = (Logger) factory(Logger.class).getNew();
            logger.critical("Could not make a ComponentFactory for class ",
                    persistentClass.getName(),
                    " due to ", e.getClass().getSimpleName(), " : ",
                    e.getMessage());
            throw new CriticalError();
        }
    }

    public Object getNew() {
        if (singletons.containsKey(persistentClass.getName()))
            return singletons.get(persistentClass.getName());

        Object[] dependenciesNewed = new Object[constructor.getParameterCount()];
        for (int i = 0; i < dependencies.size(); i++)
            dependenciesNewed[i] = dependencies.get(i).getNew();

        constructor.setAccessible(true);
        try {
            return constructor.newInstance(dependenciesNewed);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Logger logger = (Logger) factory(Logger.class).getNew();
            logger.critical("Could not make a ComponentFactory for class ",
                    persistentClass.getName(),
                    " due to ", e.getClass().getSimpleName(), " : ",
                    e.getMessage());
            throw new CriticalError();
        }
    }

    private void findConstructor() throws IncorrectAnnotationException, CircularDependencyException {
        for (Constructor c : persistentClass.getDeclaredConstructors()) {
            if (c.getAnnotation(Autowired.class) == null)
                continue;
            if (constructor != null)
                throw new IncorrectAnnotationException(String.format("Class %s has more than one Autowired Constructor", persistentClass.getName()));
            constructor = c;
        }

        if (constructor != null) {
            resolveDependencies();
            return;
        }

        for (Constructor c : persistentClass.getDeclaredConstructors()) {
            if (c.getParameterCount() == 0)
                constructor = c;
        }

        if (constructor == null)
            throw new IncorrectAnnotationException(String.format("Class %s has no suitable Constructors", persistentClass.getName()));
    }

    private void resolveDependencies() throws IncorrectAnnotationException, CircularDependencyException {
        isResolving = true;
        for (Parameter p : constructor.getParameters()) {
            if (p.getType().isPrimitive())
                throw new IncorrectAnnotationException(String.format("Class %s's Constructor has a primitive type", persistentClass.getName()));
            if (p.getAnnotation(Properties.class) != null) {
                String key = p.getAnnotation(Properties.class).value();
                ((Config)factory(Config.class).getNew()).get(key, p.getType());
                dependencies.add(new PropertiesComponent(key, p.getType()));
            } else if (components.containsKey(p.getType().getName())) {
                dependencies.add(components.get(p.getType().getName()));
                if (components.get(p.getType().getName()).isResolving)
                    throw new CircularDependencyException();
            } else {
                ComponentFactory c = new ComponentFactory(p.getType());

                dependencies.add(c);
            }
        }
        isResolving = false;
    }

    public static Object setSingleton(Class singletonClass, boolean set) { // true means it's going to be a singleton
        if ((singletons.containsKey(singletonClass.getName()) && set) ||
            (factory(singletonClass).singleton && !set))
            return singletons.get(singletonClass.getName());

        if (set)
            singletons.put(singletonClass.getName(), factory(singletonClass).getNew());
        else
            singletons.remove(singletonClass.getName());

        return factory(singletonClass).getNew();
    }
}
