package ru.nsu.fit.dicontainer.service;

import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.impl.Recommender;

public interface GiftChooseHelper {
  Gift choose(Person person);
  Recommender getRecommender();
}
