package ru.nsu.fit.dicontainer.factory;

//import ru.nsu.fit.dicontainer.annotation.Inject;

import javax.inject.Inject;
import javax.inject.Singleton;


import org.example.beans.BeanDefinition;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.config.Configuration;
import ru.nsu.fit.dicontainer.config.JavaConfiguration;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.*;
import static javassist.runtime.Desc.getClazz;

public class BeanFactory {
  private ApplicationContext applicationContext;
  private final Map<BeanDefinition, Object> sinletonBeanMap = new ConcurrentHashMap<>();
  private final Map<Thread ,Map<BeanDefinition, Object>> threadBeanMap = new ConcurrentHashMap<>();

  public BeanFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public <T> T getBean(Class<T> clazz) {
    BeanDefinition currentBeanDefinition = null;
    if (clazz.isInterface()){
      List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions()
          .parallelStream()
          .filter(beanDefinition -> clazz.isAssignableFrom(beanDefinition.getClazz()))
          .toList();
      if (beanDefinitionList.size() > 1){
        throw new RuntimeException("Several beans implements interface");
      }else{
        try{
          currentBeanDefinition = beanDefinitionList.get(0);
        }catch (ClassCastException ex){
          System.err.println(ex.getMessage());
          throw new RuntimeException(ex.getMessage());
        }finally {
          if (currentBeanDefinition == null){
            throw new RuntimeException("currentBeanDefinition in BeanFactory.getBean(" + clazz + ") is null");
          }
        }
      }
    }

    if (currentBeanDefinition.getScope().equals("singleton") &&
        sinletonBeanMap.containsKey(currentBeanDefinition)) {
      return (T) sinletonBeanMap.get(currentBeanDefinition);
    }
    if (currentBeanDefinition.getScope().equals("thread") &&
        threadBeanMap.containsKey(Thread.currentThread())){
      Map<BeanDefinition, Object> threadMap = threadBeanMap.get(Thread.currentThread());
      if (threadMap.containsKey(currentBeanDefinition)){
        return (T) threadBeanMap.get(currentBeanDefinition);
      }
    }

    try {
      Constructor<? extends T> constructor = (Constructor<? extends T>) currentBeanDefinition.getClazz().getDeclaredConstructor();
      Class[] parameters = constructor.getParameterTypes();


      T bean = implementationClass.getDeclaredConstructor().newInstance();
      if (implementationClass.isAnnotationPresent(ThreadScope.class)) {
        if (threadBeanMap.containsKey(Thread.currentThread())) {
          threadBeanMap.get(Thread.currentThread()).put(clazz, bean);
        } else {
          Map<Class, Object> threadMap = new ConcurrentHashMap<>();
          threadMap.put(clazz, bean);
          threadBeanMap.put(Thread.currentThread(), threadMap);
        }
      }
      if (implementationClass.isAnnotationPresent(Singleton.class)) {
        sinletonBeanMap.put(clazz, bean);
      }

      List<Field> fieldList = stream(implementationClass.getDeclaredFields()).toList();
      for (Field field : fieldList) {
        field.setAccessible(true);
        field.set(bean, applicationContext.getBean(field.getType()));
      }
      return bean;
    }catch (ClassCastException ex){
      throw new RuntimeException(ex.getMessage() + currentBeanDefinition + ".getConstructor()");
    } catch (Exception ex) {
      System.out.println("Exception in BeanFactory.getBean" + ex.getMessage());
    }
    return null;
  }

  private <T> T getPrototype(Class<T> clazz){
    //TODO method for Provider<T>
    return null;
  }
}
