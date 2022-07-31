package com.example.smtpserver.utils;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/** 保存spring上下文对象，手动获取bean */
@Component
public class ApplicationContextHolder {

  // 上下文对象
  public static ConfigurableApplicationContext context;

  /** 通过name获取 Bean. */
  public static Object getBean(String name) {
    return context.getBean(name);
  }

  /** 通过class获取Bean. */
  public static <T> T getBean(Class<T> clazz) {
    return context.getBean(clazz);
  }

  /** 通过name,以及Clazz返回指定的Bean */
  public static <T> T getBean(String name, Class<T> clazz) {
    return context.getBean(name, clazz);
  }
}
