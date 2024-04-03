package ru.nsu.fit.dicontainer;

import ru.nsu.fit.dicontainer.configurator.BeanConfigurator;
import ru.nsu.fit.dicontainer.configurator.JsonConfigurator;
import ru.nsu.fit.dicontainer.context.ApplicationContext;
import ru.nsu.fit.dicontainer.exception.BeanCurrentlyInCreationException;
import ru.nsu.fit.dicontainer.factory.BeanFactory;
import ru.nsu.fit.dicontainer.model.Person;
import ru.nsu.fit.dicontainer.service.GiftPresenter;

public class DiContainer {
  public ApplicationContext run(String packagePath, BeanConfigurator configurator) throws BeanCurrentlyInCreationException {
    ApplicationContext applicationContext = new ApplicationContext(packagePath ,configurator);
    BeanFactory beanFactory = new BeanFactory(applicationContext);
    applicationContext.setBeanFactory(beanFactory);
    return applicationContext;
  }

  public static void main(String[] args) throws BeanCurrentlyInCreationException {
    DiContainer diContainer = new DiContainer();
    BeanConfigurator jsonBeanConfigurator = new JsonConfigurator("/presentConfig.json");
    ApplicationContext context = diContainer.run("ru.nsu.fit.dicontainer", jsonBeanConfigurator);
    GiftPresenter giftPresenter = context.getBean(GiftPresenter.class);
    giftPresenter.present(new Person("Volodya"));
  }
}
