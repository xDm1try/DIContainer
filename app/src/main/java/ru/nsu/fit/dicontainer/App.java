package ru.nsu.fit.dicontainer;

import ru.nsu.fit.dicontainer.client.PartyOrganizer;
import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.configurator.JsonConfigurator;
import ru.nsu.fit.dicontainer.context.ApplicationContext;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;
import ru.nsu.fit.dicontainer.service.GiftPresenter;

public class App {
  public ApplicationContext run(){
    BeanConfigurator jsonBeanConfigurator = new JsonConfigurator("/config.json");
    ApplicationContext applicationContext = new ApplicationContext("ru.nsu.fit.dicontainer" ,jsonBeanConfigurator);
    BeanFactory beanFactory = new BeanFactory(applicationContext);
    applicationContext.setBeanFactory(beanFactory);

    return applicationContext;
  }

  public static void main(String[] args) {
    App app = new App();
    ApplicationContext context = app.run();
    Thread th2 = new Thread(() -> {
      GiftChooseHelper obj1 = context.getBean(GiftChooseHelper.class);
      System.out.println("1" + obj1);
      obj1.choose(new Person("NAME1"));
    });
    GiftChooseHelper obj2 = context.getBean(GiftChooseHelper.class);
    System.out.println("1" + obj2);
    obj2.choose(new Person("NAME2"));
    th2.start();
  }
}
