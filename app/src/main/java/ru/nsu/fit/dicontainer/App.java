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
    BeanConfigurator jsonBeanConfigurator = new JsonConfigurator("/presentConfig.json");
    ApplicationContext applicationContext = new ApplicationContext("ru.nsu.fit.dicontainer" ,jsonBeanConfigurator);
    BeanFactory beanFactory = new BeanFactory(applicationContext);
    applicationContext.setBeanFactory(beanFactory);

    return applicationContext;
  }

  public static void main(String[] args) {
    App app = new App();
    ApplicationContext context = app.run();
    GiftPresenter giftPresenter = context.getBean(GiftPresenter.class);
    giftPresenter.present(new Person("Volodya"));
  }
}
