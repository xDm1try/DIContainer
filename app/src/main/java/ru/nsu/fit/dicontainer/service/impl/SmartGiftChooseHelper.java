package ru.nsu.fit.dicontainer.service.impl;

import javax.inject.Inject;
import javax.inject.Scope;
import javax.inject.Singleton;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;

@ThreadScope
public class SmartGiftChooseHelper implements GiftChooseHelper {

  @Inject()
  private Recommender recommender;

  @Override
  public Gift choose(Person person) {
    recommender.recommend();
    System.out.println("Recommended by " + recommender.getClass().getSimpleName() + " " + recommender.hashCode());
    return new Gift("iPhone", 120000);
  }
  @PostConstruct
  public void postConstruct(){
    System.out.println("SmartGiftChooseHelper has been initialized " + this.hashCode());
  }

  public void setRecommender(Recommender recommender) {
    this.recommender = recommender;
  }
}
