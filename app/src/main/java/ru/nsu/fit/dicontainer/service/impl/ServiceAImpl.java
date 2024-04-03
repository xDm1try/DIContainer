package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.service.ServiceA;
import ru.nsu.fit.dicontainer.service.ServiceB;

import javax.inject.Inject;

public class ServiceAImpl implements ServiceA {
  @Inject
  private ServiceB serviceB;


  @Override
  public void serveA() {
    System.out.println("A is serving");
    serviceB.serveB();
  }
}
