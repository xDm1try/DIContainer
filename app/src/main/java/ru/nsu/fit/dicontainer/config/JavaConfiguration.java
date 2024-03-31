package ru.nsu.fit.dicontainer.config;

import ru.nsu.fit.dicontainer.service.PaymentSystem;
import ru.nsu.fit.dicontainer.service.impl.CashPaymentSystem;

import java.util.Map;

public class JavaConfiguration implements Configuration{
  private String packageToScan;

  public JavaConfiguration(String packageToScan) {
    this.packageToScan = packageToScan;
  }

  @Override
  public String getPackageToScan() {
    return this.packageToScan;
  }
}
