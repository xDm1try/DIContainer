package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.Inject;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;

public class SmartGiftChooseHelper implements GiftChooseHelper {
  @Inject
  private Recommender recommender;

  @Override
  public Gift choose(Person person) {
    recommender.recommend();
    System.out.println("Smart helper recommended");
    return new Gift("iPhone", 120000);
  }

}
