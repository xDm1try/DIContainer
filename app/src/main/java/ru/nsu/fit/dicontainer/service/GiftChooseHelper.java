package ru.nsu.fit.dicontainer.service;

import ru.nsu.fit.dicontainer.model.Gift;
import ru.nsu.fit.dicontainer.model.Person;

public interface GiftChooseHelper {
  Gift choose(Person person);
  Recommender getRecommender();
}
