package com.senai.ml.t1.dota.schedulers;

import com.senai.ml.t1.dota.clients.OpenDotaClient;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class HelloWorldScheduler {
  private final OpenDotaClient openDotaClient;

  public HelloWorldScheduler(OpenDotaClient openDotaClient) {
    this.openDotaClient = openDotaClient;
  }

  @Scheduled(fixedRate = "8000s")
  public void printHelloWorld1() {
    this.openDotaClient.getMatchDetails(7978465507l)
        .doOnSuccess(match -> System.out.println(match.toString()))
        .doOnError(error -> System.err.println("Error fetching match details: " + error.getMessage()))
        .subscribe();
    System.out.println("Hello World 1s");
  }
}
