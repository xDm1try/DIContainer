package ru.nsu.fit.dicontainer.configurator;

import com.google.common.reflect.Reflection;
import org.reflections.Reflections;

import java.util.List;

public interface BeanConfigurator {
  <T> List<Class<? extends T>> getBeanDefinitions(Class<T> interfaceClass);
  Reflections getScanner();
}
