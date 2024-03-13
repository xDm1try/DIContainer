package ru.nsu.fit.dicontainer.postprocessor;

import ru.nsu.fit.dicontainer.annotation.PostConstruct;

import java.lang.reflect.Method;

public class BeanConstructBeanPostProcessor implements BeanPostProcessor{
  @Override
  public void process(Object bean){
    for(Method method :bean.getClass().getDeclaredMethods()){
      if(method.isAnnotationPresent(PostConstruct.class)){
        try{
          method.invoke(bean);
        }catch (Exception e){
          System.out.println("Exception in BeanConstructBeanPostProcessor.process(" + method + ")");
        }
      }
    }
  }
}
