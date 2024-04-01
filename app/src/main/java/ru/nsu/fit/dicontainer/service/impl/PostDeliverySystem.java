package ru.nsu.fit.dicontainer.service.impl;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.Courier;
import ru.nsu.fit.dicontainer.service.DeliverySystem;
import ru.nsu.fit.dicontainer.service.Device;
import ru.nsu.fit.dicontainer.service.Vehicle;

public class PostDeliverySystem implements DeliverySystem {
  Vehicle vehicle;
  Courier courier;
  Device device;
  public PostDeliverySystem(Vehicle vehicle, Courier courier, Device device) {
    this.vehicle = vehicle;
    this.courier = courier;
    this.device = device;
  }

  @PostConstruct
  public void postConstruct(){
    System.out.println("PostDeliverySystem has been initialized " + this.hashCode());
  }
  @Override
  public void deliver(Gift gift, Person person) {
    System.out.println(this.getClass().getSimpleName() + "is posting " + gift.getName() + " to " + person.getName());
    courier.postGift();
    device.navigate();
    vehicle.relocate();
  }
}
