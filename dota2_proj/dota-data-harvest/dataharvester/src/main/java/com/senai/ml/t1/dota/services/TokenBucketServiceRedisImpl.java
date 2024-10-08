package com.senai.ml.t1.dota.services;

import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Singleton
@Primary
public class TokenBucketServiceRedisImpl implements TokenBucketService {
  private final StatefulRedisConnection<String, String> connection;

  public TokenBucketServiceRedisImpl(StatefulRedisConnection<String, String> connection) {
    this.connection = connection;
  }

  @Override
  public int availableDailyTokens(String apiKey) {
    return 0;
  }

  @Override
  public int availableMinuteTokens(String apiKey) {
    return 0;
  }

  @Override
  public void refilDailyRequestTokens(String apiKey) {
  }

  @Override
  public void refilMinuteRequestTokens(String apiKey) {
  }
}
