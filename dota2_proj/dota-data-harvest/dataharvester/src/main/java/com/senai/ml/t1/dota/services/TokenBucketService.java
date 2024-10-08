package com.senai.ml.t1.dota.services;

public interface TokenBucketService {
  public int availableDailyTokens(String apiKey);

  public int availableMinuteTokens(String apiKey);

  public void refilDailyRequestTokens(String apiKey);

  public void refilMinuteRequestTokens(String apiKey);
}
