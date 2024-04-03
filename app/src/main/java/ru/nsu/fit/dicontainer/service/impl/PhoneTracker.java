package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.service.Device;

public class PhoneTracker implements Device {
  @Override
  public void navigate() {
    System.out.println("PhoneTracker for navigation");
  }
}
