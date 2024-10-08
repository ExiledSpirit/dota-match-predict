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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'availableDailyTokens'");
  }

  @Override
  public int availableMinuteTokens(String apiKey) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'availableMinuteTokens'");
  }

  @Override
  public void refilDailyRequestTokens(String apiKey) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'refilDailyRequestTokens'");
  }

  @Override
  public void refilMinuteRequestTokens(String apiKey) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'refilMinuteRequestTokens'");
  }
}
