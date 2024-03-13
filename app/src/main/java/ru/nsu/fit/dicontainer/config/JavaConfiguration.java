package ru.nsu.fit.dicontainer.config;

import ru.nsu.fit.dicontainer.service.PaymentSystem;
import ru.nsu.fit.dicontainer.service.impl.CashPaymentSystem;

import java.util.Map;

public class JavaConfiguration implements Configuration{
  @Override
  public String getPackageToScan() {
    return "ru.nsu.fit.dicontainer";
  }

  @Override
  public Map<Class, Class> getInterfaceToImplementation() {
    return Map.of(PaymentSystem.class, CashPaymentSystem.class);
  }
}
