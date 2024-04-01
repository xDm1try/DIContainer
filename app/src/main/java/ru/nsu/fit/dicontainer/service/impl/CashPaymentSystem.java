package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.service.PaymentSystem;

import javax.inject.Singleton;
import java.sql.SQLOutput;
@Singleton
public class CashPaymentSystem implements PaymentSystem {

  @PostConstruct
  public void postConstruct(){
    System.out.println("CashPaymentSystem has been initialized " + this.hashCode());
  }
  @Override
  public void pay(Gift gift) {
    System.out.println("Cash payment was done");
  }
}
