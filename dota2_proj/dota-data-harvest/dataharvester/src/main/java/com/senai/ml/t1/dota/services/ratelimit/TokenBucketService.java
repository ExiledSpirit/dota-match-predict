package com.senai.ml.t1.dota.services.ratelimit;

public interface TokenBucketService {
  public int availableDailyTokens();

  public int availableMinuteTokens();

  public void refilDailyRequestTokens();

  public void refilMinuteRequestTokens();

  public boolean consumeToken();
}
