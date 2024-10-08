package com.senai.ml.t1.dota.schedulers;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class HelloWorldScheduler {
  @Scheduled(fixedRate = "80s", condition = "")
  public void printHelloWorld1() {
    System.out.println("Hello World 1s");
  }
}
