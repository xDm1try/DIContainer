package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;

import javax.inject.Singleton;

@Singleton
public class SmartRecommender implements Recommender{
  @PostConstruct
  public void postConstruct(){
    System.out.println("SmartRecommender has been initialized " + this.hashCode());
  }
  @Override
  public void recommend() {
    System.out.println("Smart recommender recommended" + this.hashCode());
  }
}
