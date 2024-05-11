package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.IncorrectAnnotationException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import ir.doodmood.mashtframework.annotation.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ComponentFactory {
    private final ArrayList<ComponentFactory> dependencies;
    private Constructor constructor;
    private final Class persistentClass;
    private static final HashMap<String, ComponentFactory> components = new HashMap<>();
    private boolean isResolving = false;

    public ComponentFactory(Class persistentClass) throws IncorrectAnnotationException, CircularDependencyException {
        this.persistentClass = persistentClass;
        dependencies = new ArrayList<>();
        constructor = null;

        if (persistentClass.getAnnotation(Component.class) == null)
            throw new IncorrectAnnotationException(String.format("Class %s does not annotate Component", persistentClass.getName()));

        components.put(persistentClass.getName(), this);

        findConstructor();
    }

    public Object getNew() {
        Object[] dependenciesNewed = new Object[constructor.getParameterCount()];
        for (int i = 0; i < dependencies.size(); i++)
            dependenciesNewed[i] = dependencies.get(i).getNew();

        constructor.setAccessible(true);
        try {
            return constructor.newInstance(dependenciesNewed);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // TODO: add logging
            e.printStackTrace();
            return null;
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
            if (components.containsKey(p.getType().getName())) {
                dependencies.add(components.get(p.getType().getName()));
                if (components.get(p.getType().getName()).isResolving)
                    throw new CircularDependencyException();
            } else {
                // TODO: maybe implement some annotations to inject data
                //  into constructors? for the unannotated primitive thingy down here.

                if (p.getType().isPrimitive())
                    throw new IncorrectAnnotationException(String.format("Class %s's Constructor has an unannotated primitive type", persistentClass.getName()));
                ComponentFactory c = new ComponentFactory(p.getType());

                dependencies.add(c);
            }
        }
        isResolving = false;
    }
}
