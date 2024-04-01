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
  public ApplicationContext run(String packagePath, BeanConfigurator configurator){

    ApplicationContext applicationContext = new ApplicationContext(packagePath ,configurator);
    BeanFactory beanFactory = new BeanFactory(applicationContext);
    applicationContext.setBeanFactory(beanFactory);

    return applicationContext;
  }

  public static void main(String[] args) {
    App app = new App();
    BeanConfigurator jsonBeanConfigurator = new JsonConfigurator("/presentConfig.json");
    ApplicationContext context = app.run("ru.nsu.fit.dicontainer", jsonBeanConfigurator);
    GiftPresenter giftPresenter = context.getBean(GiftPresenter.class);
    giftPresenter.present(new Person("Volodya"));
  }
}
