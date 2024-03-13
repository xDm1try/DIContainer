package ru.nsu.fit.dicontainer.configurator;

import com.google.common.reflect.Reflection;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator{
  @Override
  public Reflections getScanner() {
    return scanner;
  }

  private final Reflections scanner;
  private final Map<Class, Class> interfaceToImplementation;
  public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImplementation) {
    this.scanner = new Reflections(packageToScan);
    this.interfaceToImplementation = new ConcurrentHashMap<>(interfaceToImplementation);
  }

  @Override
  public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
    return interfaceToImplementation.computeIfAbsent(interfaceClass, clazz -> {
      Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
      if(implementationClasses.size() != 1){
        throw new RuntimeException("Interface has 0 or more than 1 implementations");
      }
      return implementationClasses.stream().findFirst().get();
    });
  }
}
