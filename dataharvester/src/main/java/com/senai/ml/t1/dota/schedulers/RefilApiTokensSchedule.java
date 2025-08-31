package com.senai.ml.t1.dota.schedulers;

import com.senai.ml.t1.dota.configuration.opendotaratelimit.TokenBucketService;

import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class RefilApiTokensSchedule {
  @Value("${api.key}")
  private String apiKey;

  private final TokenBucketService tokenBucketService;

  public RefilApiTokensSchedule(TokenBucketService tokenBucketService) {
    this.tokenBucketService = tokenBucketService;
  }

  @Scheduled(fixedRate = "1m")
  public void refilMinuteRequestTokens() {
    this.tokenBucketService.refilMinuteRequestTokens();
  }

  @Scheduled(fixedRate = "24h")
  public void refilDailyRequestTokens() {
    this.tokenBucketService.refilDailyRequestTokens();
  }
}
