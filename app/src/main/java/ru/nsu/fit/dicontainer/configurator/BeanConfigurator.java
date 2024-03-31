package ru.nsu.fit.dicontainer.configurator;

import com.google.common.reflect.Reflection;
import org.example.beans.BeanDefinition;
import org.reflections.Reflections;

import java.util.List;

public interface BeanConfigurator {
  List<BeanDefinition> getBeanDefinitions();
}
