package ru.nsu.fit.dicontainer.context;

import lombok.Setter;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.postprocessor.BeanPostProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
  @Setter
  private BeanFactory beanFactory;
  private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

  public ApplicationContext() {

  }

  public <T> T getBean(Class<T> clazz) {
    if (beanMap.containsKey(clazz)) {
      return (T) beanMap.get(clazz);
    }

    T bean = beanFactory.getBean(clazz);
    callPostProcessors(bean);

    beanMap.put(clazz, bean);
    return bean;
  }

  private void callPostProcessors(Object bean) {
    for(Class processor : beanFactory.getBeanConfigurator().getScanner().getSubTypesOf(BeanPostProcessor.class)){
      BeanPostProcessor postProcessor = null;
      try {
        postProcessor = (BeanPostProcessor) processor.getDeclaredConstructor().newInstance();
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
      postProcessor.process(bean);
    }
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
}
