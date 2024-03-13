package ru.nsu.fit.dicontainer.client;

import ru.nsu.fit.dicontainer.annotation.Inject;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftPresenter;

public class PartyOrganizer {

  @Inject
  private GiftPresenter giftPresenter;
  public void prepareToCelebration(){
    Person friend = new Person("Святослав");
    giftPresenter.present(friend);
  }
}
