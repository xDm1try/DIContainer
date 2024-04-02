package ru.nsu.fit.dicontainer.factory.provider.impl;

import ru.nsu.fit.dicontainer.service.GiftChooseHelper;
import ru.nsu.fit.dicontainer.service.impl.SmartGiftChooseHelper;

import javax.inject.Provider;

public class ProviderGiftChooseHelper implements Provider<GiftChooseHelper> {
  @Override
  public GiftChooseHelper get() {
    return new SmartGiftChooseHelper();
  }
}
