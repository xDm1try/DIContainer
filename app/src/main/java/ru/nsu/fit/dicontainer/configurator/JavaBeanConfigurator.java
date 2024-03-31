package ru.nsu.fit.dicontainer.configurator;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.reflections.Reflections;
import ru.nsu.fit.dicontainer.annotation.Configuration;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator{
  @Override
  public <T> List<Class<? extends T>> getBeanDefinitions(Class<T> interfaceClass) {
    return null;
  }

  @Override
  public Reflections getScanner() {
    return scanner;
  }

  private final Reflections scanner;
  private final Map<Class, Class> interfaceToBeanDefinition;
  public JavaBeanConfigurator(String packageToScan) {
    this.scanner = new Reflections(packageToScan);
    this.interfaceToBeanDefinition = new ConcurrentHashMap<>();
    List<Class<?>> configClasses = new ArrayList<>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    File packageDir = new File(classLoader.getResource(packageToScan).getFile());
    scanDirectory(packageDir, packageToScan, configClasses);
    configClasses.stream().flatMap(clazz -> Arrays.stream(clazz.getMethods())).map(method -> {
      Class<?> interf = method.getReturnType();
      String scope = null;
      if (method.isAnnotationPresent(ThreadScope.class)){
        scope = "thread";
      }else if(method.isAnnotationPresent(Prototype.class)) {
        scope = "prototype";
      }else{
        scope = "singleton";
      }
      Class<?>[] parameters = method.getParameterTypes();
      List<Class<?>> parametersList = Arrays.asList(parameters);



    });

  }


  private void scanDirectory(File directory, String packageName, List<Class<?>> configClasses){
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          scanDirectory(file, packageName + "." + file.getName(), configClasses);
        } else if (file.getName().endsWith(".class")) {
          String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
          try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Configuration.class)) {
              configClasses.add(clazz);
            }
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    }
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
