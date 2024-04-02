package ru.nsu.fit.dicontainer.context;

import lombok.Getter;
import lombok.Setter;
import org.example.beans.BeanDefinition;
import org.reflections.Reflections;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.config.Configuration;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.dfs.FinderCycle;
import ru.nsu.fit.dicontainer.exception.BeanCurrentlyInCreationException;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.postprocessor.BeanPostProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApplicationContext {
  @Setter
  private BeanFactory beanFactory;
  @Getter
  private String packageToScan;
  @Getter
  private Reflections scanner;
  private List<BeanConfigurator> beanConfigurators = new ArrayList<>();
  private List<BeanDefinition> beanDefinitions;

  public ApplicationContext(String packageToScan, BeanConfigurator beanConfigurator) throws BeanCurrentlyInCreationException {
    this.packageToScan = packageToScan;
    this.scanner = new Reflections(packageToScan);
    this.beanConfigurators.add(beanConfigurator);
    this.beanDefinitions = this.beanConfigurators.stream()
        .flatMap(beanConfigurator1 -> beanConfigurator1.getBeanDefinitions().stream())
        .distinct()
        .toList();
    if(FinderCycle.hasCycleReferences(this.beanDefinitions)){
      throw new BeanCurrentlyInCreationException();
    }
  }

  public ApplicationContext(String packageToScan, List<BeanConfigurator> beanConfigurators) throws BeanCurrentlyInCreationException {
    this.packageToScan = packageToScan;
    this.beanConfigurators.addAll(beanConfigurators);
    this.beanDefinitions = this.beanConfigurators.stream()
        .flatMap(beanConfigurator1 -> beanConfigurator1.getBeanDefinitions().stream())
        .distinct()
        .toList();
    if(FinderCycle.hasCycleReferences(this.beanDefinitions)){
      throw new BeanCurrentlyInCreationException();
    }
  }

  public <T> T getBean(Class<T> clazz) {
    T bean = beanFactory.getBean(clazz);
    callPostProcessors(bean);
    return bean;
  }

  public <T> T getBean(String name) {
    T bean = beanFactory.getBean(name);
    callPostProcessors(bean);
    return bean;
  }

  public List<BeanDefinition> getAllBeanDefinitions() {
    return new ArrayList<>(this.beanDefinitions);
  }

  private void callPostProcessors(Object bean) {
    for (Class processor : scanner.getSubTypesOf(BeanPostProcessor.class)) {
      BeanPostProcessor postProcessor = null;
      try {
        postProcessor = (BeanPostProcessor) processor.getDeclaredConstructor().newInstance();
        postProcessor.process(bean);
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }

    }
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
}
