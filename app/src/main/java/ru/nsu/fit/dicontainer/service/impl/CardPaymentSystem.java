package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.service.PaymentSystem;

public class CardPaymentSystem implements PaymentSystem {
  public CardPaymentSystem() {
  }

  @Override
  public void pay(Gift gift) {
    System.out.println("Card payment was done");
  }
}
