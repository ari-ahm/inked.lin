package ir.doodmood.mashtframework.core;

import ir.doodmood.mashtframework.exception.CircularDependencyException;
import ir.doodmood.mashtframework.exception.IncorrectAnnotationException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
class ComponentFactory {
    private ArrayList<ComponentFactory> dependencies;
    private Constructor constructor;
    private final Class persistentClass;
    private final HashMap<String, ComponentFactory> components;
    private int circularDependencyCount = 0;

    public ComponentFactory(Class persistentClass, HashMap<String, ComponentFactory> components) throws IncorrectAnnotationException, CircularDependencyException {
        this.persistentClass = persistentClass;
        this.components = components;
        dependencies = new ArrayList<>();

        if (persistentClass.getAnnotation(ir.doodmood.mashtframework.annotation.Component.class) == null)
            throw new IncorrectAnnotationException(String.format("Class %s does not annotate Component", persistentClass.getName()));

        components.put(persistentClass.getName(), this);

        findConstructor();
        if (!circularDependencyCheck())
            throw new CircularDependencyException("There is a circular dependency in your code...");
    }

    public Object getNew() throws IncorrectAnnotationException {
        Object[] dependenciesNewed = new Object[constructor.getParameterCount()];
        for (int i = 0; i < dependencies.size(); i++)
            dependenciesNewed[i] = dependencies.get(i).getNew();

        constructor.setAccessible(true);
        try {
            return constructor.newInstance(dependenciesNewed);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void findConstructor() throws IncorrectAnnotationException, CircularDependencyException {
        boolean autoWired = false;
        for (Constructor c : persistentClass.getDeclaredConstructors()) {
            if (c.getParameterCount() == 0 && !autoWired)
                constructor = c;
            if (c.getAnnotation(ir.doodmood.mashtframework.annotation.Autowired.class) == null)
                continue;

            if (autoWired)
                throw new IncorrectAnnotationException(String.format("Class %s has more than one Autowired Constructor", persistentClass.getName()));

            autoWired = true;

            constructor = c;
            resolveDependencies();
        }

        if (constructor == null)
            throw new IncorrectAnnotationException(String.format("Class %s has no suitable Constructors", persistentClass.getName()));
    }

    private void resolveDependencies() throws IncorrectAnnotationException, CircularDependencyException {
        dependencies.clear();

        for (Parameter p : constructor.getParameters()) {
            if (components.containsKey(p.getType().getName()))
                dependencies.add(components.get(p.getType().getName()));
            else {
                // TODO: maybe implement some annotations to inject data
                //  into constructors? for the unannotated primitive thingy down here.

                if (p.getType().isPrimitive())
                    throw new IncorrectAnnotationException(String.format("Class %s's Constructor has an unannotated primitive type", persistentClass.getName()));
                ComponentFactory c = new ComponentFactory(p.getType(), components);

                dependencies.add(c);
            }
        }
    }

    private boolean circularDependencyCheck() {
        circularDependencyCount++;
        if (circularDependencyCount > 1) {
            circularDependencyCount = 0;
            return false;
        }
        for (ComponentFactory c : dependencies) {
            if (!c.circularDependencyCheck()) {
                circularDependencyCount = 0;
                return false;
            }
        }
        circularDependencyCount--;
        return true;
    }
}
