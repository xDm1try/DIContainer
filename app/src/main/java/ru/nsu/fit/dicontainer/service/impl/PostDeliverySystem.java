package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.DeliverySystem;

import javax.inject.Singleton;

@Singleton
public class PostDeliverySystem implements DeliverySystem {
  @PostConstruct
  public void postConstruct(){
    System.out.println("PostDeliverySystem has been initialized " + this.hashCode());
  }
  @Override
  public void deliver(Gift gift, Person person) {
    System.out.println(this.getClass().getSimpleName() + "is posting " + gift.getName() + " to " + person.getName());
  }
}
