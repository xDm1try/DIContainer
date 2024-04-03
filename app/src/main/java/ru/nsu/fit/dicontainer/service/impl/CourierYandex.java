package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.service.Courier;

public class CourierYandex implements Courier {
  String phrase;
  @Override
  public void postGift() {
    System.out.println("CourierYandex for posting said: ");
    this.sayPhrase();
  }

  public CourierYandex(String phrase) {
    this.phrase = phrase;
  }

  @Override
  public void sayPhrase() {
    System.out.println(this.phrase);
  }
}
