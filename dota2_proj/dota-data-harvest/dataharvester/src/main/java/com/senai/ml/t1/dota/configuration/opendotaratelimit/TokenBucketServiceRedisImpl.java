package com.senai.ml.t1.dota.configuration.opendotaratelimit;

import com.senai.ml.t1.dota.configuration.RateLimitConfiguration;

import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Singleton
@Primary
public class TokenBucketServiceRedisImpl implements TokenBucketService {
  private static final String MINUTE_TOKENS = "minute_tokens";
  private static final String DAILY_TOKENS = "daily_tokens";

  private RateLimitConfiguration rateLimitConfiguration;
  private final StatefulRedisConnection<String, String> connection;

  public TokenBucketServiceRedisImpl(StatefulRedisConnection<String, String> connection,
      RateLimitConfiguration rateLimitConfiguration) {
    this.rateLimitConfiguration = rateLimitConfiguration;
    this.connection = connection;
  }

  @Override
  public int availableDailyTokens() {
    String key = DAILY_TOKENS;
    String tokens = connection.sync().get(key);
    return tokens != null ? Integer.parseInt(tokens) : rateLimitConfiguration.getDaily();
  }

  @Override
  public int availableMinuteTokens() {
    String key = MINUTE_TOKENS;
    String tokens = connection.sync().get(key);
    return tokens != null ? Integer.parseInt(tokens) : rateLimitConfiguration.getMinute();
  }

  @Override
  public void refilDailyRequestTokens() {
    String key = DAILY_TOKENS;
    connection.sync().set(key, String.valueOf(rateLimitConfiguration.getDaily()));
    // Optionally set the expiration time to 24 hours
    connection.sync().expire(key, 24 * 60 * 60);
  }

  @Override
  public void refilMinuteRequestTokens() {
    String key = MINUTE_TOKENS;
    connection.sync().set(key, String.valueOf(rateLimitConfiguration.getMinute()));
    // Optionally set the expiration time to 60 seconds
    connection.sync().expire(key, 60);
  }

  public boolean consumeToken() {
    String dailyKey = DAILY_TOKENS;
    String minuteKey = MINUTE_TOKENS;

    // Start a Redis transaction
    connection.sync().multi();

    // Decrement tokens for both daily and minute counters
    Long dailyTokens = connection.sync().decr(dailyKey);
    Long minuteTokens = connection.sync().decr(minuteKey);

    // Check if the transaction needs to be discarded due to limits being exceeded
    if (dailyTokens != null && dailyTokens < 0 && minuteTokens != null && minuteTokens < 0) {
      connection.sync().discard();
      return false; // No token available
    }

    // Commit the transaction
    connection.sync().exec();
    return true; // Token consumed successfully
  }
}
