package ru.nsu.fit.dicontainer.configurator;

import com.google.common.reflect.Reflection;
import org.reflections.Reflections;

public interface BeanConfigurator {
  <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
  Reflections getScanner();
}
