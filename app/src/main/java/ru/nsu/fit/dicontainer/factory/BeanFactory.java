package ru.nsu.fit.dicontainer.factory;

import org.example.beans.BeanDefinition;
import org.example.model.ConstructorArg;
import org.example.model.Property;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class BeanFactory {
  private ApplicationContext applicationContext;
  private final Map<BeanDefinition, Object> sinletonBeanMap = new ConcurrentHashMap<>();
  private final Map<Thread, Map<BeanDefinition, Object>> threadBeanMap = new ConcurrentHashMap<>();

  public BeanFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public <T> T getBean(Class<T> clazz) {
    BeanDefinition currentBeanDefinition = null;

    List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions()
        .stream()
        .filter(beanDefinition -> clazz.isAssignableFrom(beanDefinition.getClazz()))
        .toList();
    if (beanDefinitionList.size() != 1) {
      throw new RuntimeException("Not 1 beans implements interface");
    } else {
      try {
        currentBeanDefinition = beanDefinitionList.get(0);
      } catch (ClassCastException ex) {
        System.err.println(ex.getMessage());
        throw new RuntimeException(ex.getMessage());
      } finally {
        if (currentBeanDefinition == null) {
          throw new RuntimeException("currentBeanDefinition in BeanFactory.getBean(" + clazz + ") is null");
        }
      }
    }


    Optional<Object> foundBean = findBeanInMaps(currentBeanDefinition);
    if (findBeanInMaps(currentBeanDefinition).isPresent()) {
      return (T) foundBean.get();
    }

    try {
      T bean = (T) createBean(currentBeanDefinition);
      return bean;
    } catch (ClassCastException ex) {
      throw new RuntimeException("Cast after createBean(" + currentBeanDefinition + ")");
    }

  }

  public <T> T getBean(String name) {
    BeanDefinition currentBeanDefinition = null;
    List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions().stream()
        .filter(beanDefinition -> beanDefinition.getName().equals(name))
        .toList();
    if (beanDefinitionList.size() != 1) {
      throw new RuntimeException("Not 1 beanDefinition with name=" + name);
    } else {
      currentBeanDefinition = beanDefinitionList.get(0);
    }
    Optional<Object> foundBean = findBeanInMaps(currentBeanDefinition);
    if (findBeanInMaps(currentBeanDefinition).isPresent()) {
      return (T) foundBean.get();
    }
    try {
      T bean = (T) createBean(currentBeanDefinition);
      return bean;
    } catch (ClassCastException ex) {
      throw new RuntimeException("Cast after createBean(" + currentBeanDefinition + ")");
    }
  }

  private Object createBean(BeanDefinition beanDefinition) {
    Class<?> implementationClass = beanDefinition.getClazz();
    List<Property> properties = beanDefinition.getProperties();
    List<ConstructorArg> parameters = beanDefinition.getConstructorArgs();
    Object bean;

    try {
      List<Object> constructorBeans = new ArrayList<>();
      Constructor<?> constructor = implementationClass.getDeclaredConstructor();
      if (parameters == null || parameters.isEmpty()) {
        bean = constructor.newInstance();
      } else {
        parameters.stream()
            .forEach(parameter -> {
              if (parameter.getValue() == parameter.getRef()) {
                throw new RuntimeException("Need value != ref in constructor params");
              }
              if (parameter.getValue() != null) {
                constructorBeans.add(parameter.getValue());
              }
              if (parameter.getRef() != null) {
                constructorBeans.add(this.applicationContext.getBean(parameter.getRef()));
              }
            });
        Object[] paramArgs = constructorBeans.toArray();
        bean = constructor.newInstance(paramArgs);
      }
      stream(beanDefinition.getClazz().getDeclaredFields())
          .forEach(field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Inject.class)) {
              Class<?> type = field.getType();
              if (type.getTypeName().equals("".getClass().getTypeName())) {
                List<Property> valueProperties = properties.stream()
                    .filter(property -> property.getValue() != null)
                    .toList();
                if (valueProperties.size() != 1) {
                  throw new RuntimeException("Not single value \"value\" in setting properties");
                } else {
                  String value = valueProperties.get(0).getValue();
                  try {
                    field.set(bean, value);
                  } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage() + "in @Inject field.set value=" + value);
                  }
                }
              } else {
                List<BeanDefinition> refBeans = this.applicationContext.getAllBeanDefinitions().stream()
                    .filter(beanDefinition1 -> {
                      Class<?> beanImpl = beanDefinition1.getClazz();
                      boolean result = type.isAssignableFrom(beanImpl);
                      return result;
                    })
                    .toList();
                if (refBeans.size() > 1) {
                  throw new RuntimeException("More 1 value \"ref\" in setting properties");
                } else {
                  BeanDefinition injectingBean = refBeans.get(0);
                  try {
                    field.set(bean, applicationContext.getBean(injectingBean.getName()));
                  } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage() + "in @Inject field.set ref=" + injectingBean);
                  }
                }
              }
            } else if (field.isAnnotationPresent(Named.class)) {
              Named value = field.getAnnotation(Named.class);
              String beanName = value.value();
              try {
                field.set(bean, applicationContext.getBean(beanName));
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage() + "in @Named field.set");
              }
            }
          });

      putBeanInMaps(beanDefinition, bean);

    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    return bean;
  }


  private Optional<Object> findBeanInMaps(BeanDefinition beanDefinition) {
    if (beanDefinition.getScope().equals("singleton")) {
      List<Object> beansObj = this.sinletonBeanMap.entrySet().stream().filter(entry ->
          entry.getKey().equals(beanDefinition)
      ).map(entry -> entry.getValue()).toList();
      if (beansObj.size() > 1) {
        throw new RuntimeException("More 1 beanObj in singleton map");
      }
      return beansObj.stream().findFirst();
    }
    if (beanDefinition.getScope().equals("thread")) {
      List<Object> beansObj = this.threadBeanMap.entrySet().stream()
          .filter(threadMapEntry -> threadMapEntry.getKey() == Thread.currentThread())
          .map(Map.Entry::getValue)
          .flatMap(map -> map.entrySet().stream())
          .filter(entry -> entry.getKey() == beanDefinition)
          .map(entry -> entry.getValue())
          .toList();
      if (beansObj.size() > 1) {
        throw new RuntimeException("More 1 beanObj in thread map");
      }
      return beansObj.stream().findFirst();
    }
    return Optional.empty();
  }

  private void putBeanInMaps(BeanDefinition beanDefinition, Object bean) {
    if (beanDefinition.getScope().equals("thread")) {
      if (threadBeanMap.containsKey(Thread.currentThread())) {
        threadBeanMap.get(Thread.currentThread()).put(beanDefinition, bean);
      } else {
        Map<BeanDefinition, Object> threadMap = new ConcurrentHashMap<>();
        threadMap.put(beanDefinition, bean);
        threadBeanMap.put(Thread.currentThread(), threadMap);
      }
    }
    if (beanDefinition.getScope().equals("singleton")) {
      sinletonBeanMap.put(beanDefinition, bean);
    }

  }

  private <T> T getPrototype(Class<T> clazz) {
    //TODO method for Provider<T>
    return null;
  }
}
