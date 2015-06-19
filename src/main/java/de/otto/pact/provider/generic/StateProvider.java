package de.otto.pact.provider.generic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface StateProvider {

    default void setState(final String stateName) {
        try {
            List<Method> annotatedMethods = new ArrayList<>();
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(State.class) && method.getAnnotation(State.class).value().equals(stateName)) {
                    annotatedMethods.add(method);
                }
            }

            if (annotatedMethods.isEmpty()) {
                throw new RuntimeException("Define the state, dude: " + stateName);
            }

            annotatedMethods.get(0).invoke(this);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("some error occurs", e);
        }
    }
}
