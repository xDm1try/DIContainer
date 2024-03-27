package ru.nsu.fit.dicontainer.configurator;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;

public class MethodInterceptorAdvice {
  @Advice.OnMethodEnter
  public static void enter(@Advice.Origin Method method) {
    // Добавляем вызываемый метод в набор
    calledMethods.add(method);
  }
}