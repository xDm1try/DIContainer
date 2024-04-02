package ru.nsu.fit.dicontainer.factory;

import org.example.beans.BeanDefinition;
import org.example.model.ConstructorArg;
import org.example.model.Property;
import ru.nsu.fit.dicontainer.context.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;

public class BeanFactory {
    private ApplicationContext applicationContext;
    private final Map<BeanDefinition, Object> sinletonBeanMap = new ConcurrentHashMap<>();
    private final Map<Thread, Map<BeanDefinition, Object>> threadBeanMap = new ConcurrentHashMap<>();

    public BeanFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> clazz) {
        BeanDefinition currentBeanDefinition = null;
        List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions()
                .stream()
                .filter(beanDefinition -> clazz.isAssignableFrom(beanDefinition.getClazz()))
                .toList();
        if (beanDefinitionList.size() != 1) {
            throw new RuntimeException("Not 1 beans implements interface" + clazz);
        } else {
            try {
                currentBeanDefinition = beanDefinitionList.get(0);
            } catch (ClassCastException ex) {
                System.err.println(ex.getMessage());
                throw new RuntimeException(ex.getMessage());
            } finally {
                if (currentBeanDefinition == null) {
                    throw new RuntimeException("currentBeanDefinition in BeanFactory.getBean(" + clazz + ") is null");
                }
            }
        }

        Optional<Object> foundBean = findBeanInMaps(currentBeanDefinition);
        if (findBeanInMaps(currentBeanDefinition).isPresent()) {
            return (T) foundBean.get();
        }

        try {
            T bean = (T) createBean(currentBeanDefinition);
            return bean;
        } catch (ClassCastException ex) {
            throw new RuntimeException("Cast after createBean(" + currentBeanDefinition + ")");
        }

    }

    public <T> T getBean(String name) {
        BeanDefinition currentBeanDefinition = null;
        List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions().stream()
                .filter(beanDefinition -> beanDefinition.getName().equals(name))
                .toList();
        if (beanDefinitionList.size() != 1) {
            throw new RuntimeException("Not 1 beanDefinition with name=" + name);
        } else {
            currentBeanDefinition = beanDefinitionList.get(0);
        }
        Optional<Object> foundBean = findBeanInMaps(currentBeanDefinition);
        if (findBeanInMaps(currentBeanDefinition).isPresent()) {
            return (T) foundBean.get();
        }
        try {
            T bean = (T) createBean(currentBeanDefinition);
            return bean;
        } catch (ClassCastException ex) {
            throw new RuntimeException("Cast after createBean(" + currentBeanDefinition + ")");
        }
    }

    private Object[] getConstructorBeans(List<ConstructorArg> parameters) {
        List<Object> constructorBeans = new ArrayList<>();
        parameters
                .forEach(parameter -> {
                    if (Objects.equals(parameter.getValue(), parameter.getRef())) {
                        throw new RuntimeException("Need value != ref in constructor params");
                    }
                    if (parameter.getValue() != null) {
                        constructorBeans.add(parameter.getValue());
                    }
                    if (parameter.getRef() != null) {
                        try {
                            if (parameter.getPathToRef() != null) {
                                Class<?> clazz = parameter.getPathToRef();
                                constructorBeans.add(this.applicationContext.getBean(clazz));
                            } else {
                                Class<?> clazz = Class.forName(parameter.getRef());
                                constructorBeans.add(this.applicationContext.getBean(clazz));
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });

        return constructorBeans.toArray();

    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> implementationClass = beanDefinition.getClazz();
        List<Property> properties = beanDefinition.getProperties();
        List<ConstructorArg> parameters = beanDefinition.getConstructorArgs();
        Object bean;
        Class<?>[] parametersArray = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            ConstructorArg arg = parameters.get(i);
            if (arg.getRef() != null) {
                try {
                    if (arg.getPathToRef() != null) {
                        parametersArray[i] = arg.getPathToRef();
                    } else {
                        parametersArray[i] = Class.forName(arg.getRef());
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                parametersArray[i] = "".getClass();
            }
        }
        try {
            Constructor<?> constructor = implementationClass.getDeclaredConstructors()[0];
            if (parameters.isEmpty()) {
                bean = constructor.newInstance();
            } else {
                Object[] paramArgs = getConstructorBeans(parameters);
                bean = constructor.newInstance(paramArgs);
            }
            stream(beanDefinition.getClazz().getDeclaredFields())
                    .forEach(field -> {
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(Inject.class)) {
                            Class<?> type = field.getType();
                            if (type.isAssignableFrom(Provider.class)) {
                                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                                Class parameter = (Class) genericType.getActualTypeArguments()[0];
                                if (parameter == null) {
                                    throw new RuntimeException("Provider<Class>: Class is null!");
                                }

                                List<BeanDefinition> beanDefinitionList = this.applicationContext.getAllBeanDefinitions()
                                        .stream()
                                        .filter(beanDefinition1 -> {
                                            Class<?> beanImpl = beanDefinition1.getClazz();
                                            boolean result = parameter.isAssignableFrom(beanImpl);
                                            return result;
                                        }).toList();
                                if (beanDefinitionList.size() != 1) {
                                    throw new RuntimeException("Not 1 available beanDefinition" + beanDefinitionList);
                                }
                                CustomProvider<?> customProvider = new CustomProvider<>(this.applicationContext.getBean(parameter).getClass());
                                customProvider.setParamArgs(getConstructorBeans(Objects.requireNonNull(beanDefinitionList.get(0).getConstructorArgs())));
                                try {
                                    field.set(bean, customProvider);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (type.getTypeName().equals("".getClass().getTypeName())) {
                                List<Property> valueProperties = properties.stream()
                                        .filter(property -> property.getValue() != null)
                                        .toList();
                                if (valueProperties.size() != 1) {
                                    throw new RuntimeException("Not single value \"value\" in setting properties");
                                } else {
                                    String value = valueProperties.get(0).getValue();
                                    try {
                                        field.set(bean, value);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e.getMessage() + "in @Inject field.set value=" + value);
                                    }
                                }
                            } else {
                                List<BeanDefinition> refBeans = this.applicationContext.getAllBeanDefinitions().stream()
                                        .filter(beanDefinition1 -> {
                                            Class<?> beanImpl = beanDefinition1.getClazz();
                                            boolean result = type.isAssignableFrom(beanImpl);
                                            return result;
                                        })
                                        .toList();
                                if (refBeans.size() > 1) {
                                    throw new RuntimeException("More 1 value \"ref\" in setting properties");
                                } else {
                                    BeanDefinition injectingBean = refBeans.get(0);
                                    try {
                                        field.set(bean, applicationContext.getBean(injectingBean.getName()));
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e.getMessage() + "in @Inject field.set ref=" + injectingBean);
                                    }
                                }
                            }
                        } else if (field.isAnnotationPresent(Named.class)) {
                            Named value = field.getAnnotation(Named.class);
                            String beanName = value.value();
                            try {
                                field.set(bean, applicationContext.getBean(beanName));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e.getMessage() + "in @Named field.set");
                            }
                        }
                    });

            putBeanInMaps(beanDefinition, bean);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }


    private Optional<Object> findBeanInMaps(BeanDefinition beanDefinition) {
        if (beanDefinition.getScope().equals("singleton")) {
            List<Object> beansObj = this.sinletonBeanMap.entrySet().stream().filter(entry ->
                    entry.getKey().equals(beanDefinition)
            ).map(entry -> entry.getValue()).toList();
            if (beansObj.size() > 1) {
                throw new RuntimeException("More 1 beanObj in singleton map");
            }
            return beansObj.stream().findFirst();
        }
        if (beanDefinition.getScope().equals("thread")) {
            List<Object> beansObj = this.threadBeanMap.entrySet().stream()
                    .filter(threadMapEntry -> threadMapEntry.getKey() == Thread.currentThread())
                    .map(Map.Entry::getValue)
                    .flatMap(map -> map.entrySet().stream())
                    .filter(entry -> entry.getKey() == beanDefinition)
                    .map(entry -> entry.getValue())
                    .toList();
            if (beansObj.size() > 1) {
                throw new RuntimeException("More 1 beanObj in thread map");
            }
            return beansObj.stream().findFirst();
        }
        return Optional.empty();
    }

    private void putBeanInMaps(BeanDefinition beanDefinition, Object bean) {
        if (beanDefinition.getScope().equals("thread")) {
            if (threadBeanMap.containsKey(Thread.currentThread())) {
                threadBeanMap.get(Thread.currentThread()).put(beanDefinition, bean);
            } else {
                Map<BeanDefinition, Object> threadMap = new ConcurrentHashMap<>();
                threadMap.put(beanDefinition, bean);
                threadBeanMap.put(Thread.currentThread(), threadMap);
            }
        }
        if (beanDefinition.getScope().equals("singleton")) {
            sinletonBeanMap.put(beanDefinition, bean);
        }

    }
}
