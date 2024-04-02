package ru.nsu.fit.dicontainer.factory;

import lombok.Setter;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import javax.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CustomProvider<T> implements Provider<T> {
    private final Class<T> clazz;

    @Setter
    private Object[] paramArgs;

    public CustomProvider(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T get() {
        try {
            return clazz.getDeclaredConstructor().newInstance(this.paramArgs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
