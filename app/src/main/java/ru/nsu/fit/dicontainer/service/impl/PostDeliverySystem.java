package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.DeliverySystem;

import javax.inject.Singleton;

@Singleton
public class PostDeliverySystem implements DeliverySystem {
  public PostDeliverySystem() {
  }

  @Override
  public void deliver(Gift gift, Person person) {
    System.out.println("PostDeliverySystem is working");
  }
}
