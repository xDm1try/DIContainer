package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.service.PaymentSystem;

import java.sql.SQLOutput;

public class CashPaymentSystem implements PaymentSystem {
  @Override
  public void pay(Gift gift) {
    System.out.println("Cash payment was done");
  }
}
