package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;

import javax.inject.Singleton;

@Singleton
public class SmartRecommender implements Recommender{
  @Override
  public void recommend() {
    System.out.println("Smart recommender recommended");
  }
}
