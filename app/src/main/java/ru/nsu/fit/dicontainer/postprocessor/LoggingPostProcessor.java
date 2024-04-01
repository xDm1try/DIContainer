package ru.nsu.fit.dicontainer.postprocessor;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;

public class LoggingPostProcessor implements BeanPostProcessor {
  @Override
  public void process(Object bean) {
    System.out.println("LoggingPostProcessor: Bean " + bean.hashCode() + "is initialized");
  }
}
