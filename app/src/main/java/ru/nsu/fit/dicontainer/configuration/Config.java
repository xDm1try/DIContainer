package ru.nsu.fit.dicontainer.configuration;

import ru.nsu.fit.dicontainer.annotation.Bean;
import ru.nsu.fit.dicontainer.annotation.Configuration;
import ru.nsu.fit.dicontainer.annotation.Prototype;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;
import ru.nsu.fit.dicontainer.service.impl.Recommender;
import ru.nsu.fit.dicontainer.service.impl.SmartGiftChooseHelper;
import ru.nsu.fit.dicontainer.service.impl.SmartRecommender;

@Configuration
public class Config {

  @Bean
  @Prototype
  public GiftChooseHelper giftChooseHelper() {
    SmartGiftChooseHelper bean = new SmartGiftChooseHelper();
    bean.setRecommender(recommender());
    return bean;
  }
  @Bean
  public Recommender recommender() {
    return new SmartRecommender();
  }
}