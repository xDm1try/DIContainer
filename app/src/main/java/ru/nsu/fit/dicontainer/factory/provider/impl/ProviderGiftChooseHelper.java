package ru.nsu.fit.dicontainer.factory.provider.impl;

import ru.nsu.fit.dicontainer.annotation.ThreadScope;
import ru.nsu.fit.dicontainer.factory.provider.Provider;
import ru.nsu.fit.dicontainer.service.GiftChooseHelper;
import ru.nsu.fit.dicontainer.service.impl.SmartGiftChooseHelper;

import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;

public class ProviderGiftChooseHelper implements Provider<GiftChooseHelper> {
  @Override
  public GiftChooseHelper get() {
    return new SmartGiftChooseHelper();
  }
}
