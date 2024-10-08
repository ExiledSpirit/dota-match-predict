package com.senai.ml.t1.dota.schedulers;

import com.senai.ml.t1.dota.clients.OpenDotaClient;
import com.senai.ml.t1.dota.response.MatchResponse;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import reactor.core.Disposable;

@Singleton
public class HelloWorldScheduler {
  private final OpenDotaClient openDotaClient;

  private Disposable subscription;

  public HelloWorldScheduler(OpenDotaClient openDotaClient) {
    this.openDotaClient = openDotaClient;
  }

  @Scheduled(fixedRate = "1000s")
  public void printHelloWorld1() {
    if (this.subscription != null) this.subscription.dispose();
    this.subscription = this.openDotaClient.getMatchDetails(7978465507l)
        .map(MatchResponse::new)
        .doOnSuccess(match -> {
          System.out.println(match.toString());
        })
        .doOnError(error -> System.err.println("Error fetching match details: " + error.getMessage()))
        .subscribe();
    System.out.println("Hello World 1s");
  }
}
