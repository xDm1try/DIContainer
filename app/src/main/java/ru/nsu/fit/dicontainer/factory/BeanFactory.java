package ru.nsu.fit.dicontainer.factory;

//import ru.nsu.fit.dicontainer.annotation.Inject;

import javax.inject.Inject;
import javax.inject.Singleton;


import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.config.Configuration;
import ru.nsu.fit.dicontainer.config.JavaConfiguration;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.configurator.JavaBeanConfigurator;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.*;

public class BeanFactory {
  private final Configuration configuration;

  public BeanConfigurator getBeanConfigurator() {
    return beanConfigurator;
  }

  private final BeanConfigurator beanConfigurator;
  private ApplicationContext applicationContext;
  private final Map<Class, Object> sinletonBeanMap = new ConcurrentHashMap<>();
  private final Map<Thread ,Map<Class, Object>> threadBeanMap = new ConcurrentHashMap<>();

  public BeanFactory(ApplicationContext applicationContext) {
    this.configuration = new JavaConfiguration();
    this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementation());
    this.applicationContext = applicationContext;
  }

  public <T> T getBean(Class<T> clazz) {
    Class<? extends T> implementationClass = clazz;
    if (implementationClass.isInterface()) {
      implementationClass = beanConfigurator.getImplementationClass(implementationClass);
    }
    if (sinletonBeanMap.containsKey(clazz)) {
      return (T) sinletonBeanMap.get(clazz);
    }
    if (threadBeanMap.containsKey(Thread.currentThread())){
      Map<Class, Object> threadMap = threadBeanMap.get(Thread.currentThread());
      if (threadMap.containsKey(clazz)){
        return (T) threadBeanMap.get(clazz);
      }
    }

    try {
      T bean = implementationClass.getDeclaredConstructor().newInstance();
      if (implementationClass.isAnnotationPresent(ThreadScope.class)){
        if (threadBeanMap.containsKey(Thread.currentThread())){
          threadBeanMap.get(Thread.currentThread()).put(clazz, bean);
        }else{
          Map<Class, Object> threadMap = new ConcurrentHashMap<>();
          threadMap.put(clazz, bean);
          threadBeanMap.put(Thread.currentThread(), threadMap);
        }
      }
      if (implementationClass.isAnnotationPresent(Singleton.class))  {
        sinletonBeanMap.put(clazz, bean);
      }

      List<Field> fieldList = stream(implementationClass.getDeclaredFields()).toList();
      for (Field field : fieldList) {
        field.setAccessible(true);
        field.set(bean, applicationContext.getBean(field.getType()));
//        sinletonBeanMap.put(clazz, bean);
      }
      return bean;
    } catch (Exception ex) {
      System.out.println("Exception in BeanFactory.getBean" + ex.getMessage());
    }
    return null;
  }
}
