package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.service.ServiceA;
import ru.nsu.fit.dicontainer.service.ServiceB;

import javax.inject.Inject;

public class ServiceBImpl implements ServiceB {
  @Inject
  private ServiceA serviceA;

  public void serveB(){
    System.out.println("B is serving");
    serviceA.serveA();
  }
}
