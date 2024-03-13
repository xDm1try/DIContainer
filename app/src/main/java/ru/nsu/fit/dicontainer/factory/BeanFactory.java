package ru.nsu.fit.dicontainer.factory;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.nsu.fit.dicontainer.App;
import ru.nsu.fit.dicontainer.annotation.Inject;
import ru.nsu.fit.dicontainer.config.Configuration;
import ru.nsu.fit.dicontainer.config.JavaConfiguration;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.configurator.JavaBeanConfigurator;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import java.lang.reflect.Field;

import static java.util.Arrays.*;

public class BeanFactory {
  private final Configuration configuration;

  public BeanConfigurator getBeanConfigurator() {
    return beanConfigurator;
  }

  private final BeanConfigurator beanConfigurator;
  private ApplicationContext applicationContext;

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
    try {
      T bean = implementationClass.getDeclaredConstructor().newInstance();
      for (Field field : stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
        field.setAccessible(true);
        field.set(bean, applicationContext.getBean(field.getType()));
      }
      return bean;
    } catch (Exception ex) {
      System.out.println("Exception in BeanFactory.getBean" + ex.getMessage());
    }
    return null;
  }
}
