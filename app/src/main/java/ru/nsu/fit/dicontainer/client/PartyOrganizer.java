package ru.nsu.fit.dicontainer.client;

import javax.inject.Inject;

import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftPresenter;

@Prototype
public class PartyOrganizer {
  @Inject
  private GiftPresenter giftPresenter;
  public void prepareToCelebration(){
    Person friend = new Person("Святослав");
    //System.out.println(this.giftPresenter);
    giftPresenter.present(friend);
  }
}
