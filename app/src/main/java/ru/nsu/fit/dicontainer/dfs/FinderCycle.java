package ru.nsu.fit.dicontainer.dfs;

import org.example.beans.BeanDefinition;
import org.example.model.ConstructorArg;
import org.example.model.Property;

import java.util.*;
import java.util.stream.Collectors;

public class FinderCycle {
  public static boolean hasCycleReferences(List<BeanDefinition> beanDefinitions) {
    List<Wrapper> allWrappers = beanDefinitions.stream()
        .map(Wrapper::new).toList();
    Queue<Wrapper> queue = new ArrayDeque<>();
    boolean hasCycle = false;
    outerLoop:
    for(Wrapper wrapperCurrent: allWrappers){
      allWrappers.stream().map(wrapper1 -> wrapper1.integer = 0);
      wrapperCurrent.integer = 1;

      Set<String> dependencies = FinderCycle.getDependencies(wrapperCurrent);
      allWrappers.stream().filter(wrapper -> dependencies.contains(wrapper.beanDefinition.getName()))
          .forEach(wrapper -> {
            queue.add(wrapper);
            System.out.println(queue);
          });

      while(!queue.isEmpty()){
        Wrapper pointer = queue.poll();
        pointer.integer = 1;
        Set<String> dependenciesPointer = FinderCycle.getDependencies(pointer);
        hasCycle = allWrappers.stream().filter(wrapper -> dependenciesPointer.contains(wrapper.beanDefinition.getName()))
            .map(wrapper -> {
              queue.add(wrapper);
              return wrapper;
            }).anyMatch(wrapper -> wrapper.integer != 0);
        if (hasCycle){
          System.err.println("Maybe you have cycle dependencies. Check " + wrapperCurrent.beanDefinition.getName());
          break outerLoop;
        }
      }
    }
    return hasCycle;
  }
  private static Set<String> getDependencies(Wrapper currentWrapper){
    List<ConstructorArg> constructorArgs = currentWrapper.beanDefinition.getConstructorArgs();
    List<Property> properties = currentWrapper.beanDefinition.getProperties();
    Set<String> names = constructorArgs.stream().map(ConstructorArg::getName).collect(Collectors.toSet());
    names.addAll(properties.stream().map(Property::getName).collect(Collectors.toSet()));
    return names;
  }



}
