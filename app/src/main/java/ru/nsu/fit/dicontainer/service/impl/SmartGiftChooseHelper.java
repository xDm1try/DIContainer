package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;

import javax.inject.Inject;
import javax.inject.Provider;

@ThreadScope
public class SmartGiftChooseHelper implements GiftChooseHelper {

  @Inject
  private Provider<Recommender> recommenderProvider;

  @Override
  public Gift choose(Person person) {
    Recommender recommender = recommenderProvider.get();
    recommender.recommend();
    System.out.println("Recommended by " + recommender.getClass().getSimpleName() + " " + recommender.hashCode());
    return new Gift("iPhone", 120000);
  }

  public Recommender getRecommender() {
    return this.recommenderProvider.get();
  }

  @PostConstruct
  public void postConstruct(){
    System.out.println("SmartGiftChooseHelper has been initialized " + this.hashCode());
  }

}
