package ru.nsu.fit.dicontainer.service;

import javax.inject.Inject;
import javax.inject.Scope;
import javax.inject.Singleton;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;


public class GiftPresenter {
  @Inject
  private GiftChooseHelper giftChooseHelper;
  @Inject
  private PaymentSystem paymentSystem;
  @Inject
  private DeliverySystem deliverySystem;

  @PostConstruct
  public void postConstruct(){
    System.out.println("Gift presenter has been initialized " + this.hashCode());
  }

  public void present(Person person){
    Gift gift = giftChooseHelper.choose(person);
    System.out.println("Gift has been chosen: " + gift.getName());
    paymentSystem.pay(gift);
    deliverySystem.deliver(gift, person);
    System.out.println(this.getClass().getSimpleName() + " " + this.hashCode() + " completed work");
  }

}
