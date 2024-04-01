package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.service.Vehicle;

public class Car implements Vehicle {
  @Override
  public void relocate() {
    System.out.println("Using car for relocating");
  }
}
