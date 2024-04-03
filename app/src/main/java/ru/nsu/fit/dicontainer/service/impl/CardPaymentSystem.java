package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.service.PaymentSystem;

import javax.inject.Singleton;


public class CardPaymentSystem implements PaymentSystem {

  @PostConstruct
  public void postConstruct(){
    System.out.println("CardPaymentSystem has been initialized" + this.hashCode());
  }
  @Override
  public void pay(Gift gift) {
    System.out.println("Card payment was done");
  }
}
