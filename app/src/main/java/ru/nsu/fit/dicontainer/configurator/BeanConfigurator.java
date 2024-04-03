package ru.nsu.fit.dicontainer.configurator;

import org.example.beans.BeanDefinition;

import java.util.List;

public interface BeanConfigurator {
    List<BeanDefinition> getBeanDefinitions();
}
