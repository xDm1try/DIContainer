package ru.nsu.fit.dicontainer.dfs;

import org.example.beans.BeanDefinition;

class Wrapper {
  BeanDefinition beanDefinition;
  Integer integer = 0;

  public Wrapper(BeanDefinition beanDefinition) {
    this.beanDefinition = beanDefinition;
  }
}
