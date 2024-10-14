package com.senai.ml.t1.dota.configuration.opendotaratelimit;

import com.senai.ml.t1.dota.annotations.RateLimited;
import com.senai.ml.t1.dota.exceptions.NoRemainingDailyTokensAvailableException;
import com.senai.ml.t1.dota.exceptions.NoRemainingMinuteTokensAvailableException;

import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import jakarta.inject.Singleton;

@Singleton
@InterceptorBean(RateLimited.class)
public class RateLimitInterceptor implements MethodInterceptor<Object, Object> {

    private final TokenBucketService tokenBucketService;

    public RateLimitInterceptor(TokenBucketService tokenBucketService) {
        this.tokenBucketService = tokenBucketService;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        // Check and consume daily tokens
        int dailyTokens = tokenBucketService.availableDailyTokens();
        if (dailyTokens <= 0) {
            throw new NoRemainingDailyTokensAvailableException();
        }

        // Check and consume minute tokens
        int minuteTokens = tokenBucketService.availableMinuteTokens();
        if (minuteTokens <= 0) {
            throw new NoRemainingMinuteTokensAvailableException();
        }

        // Consume the tokens for both daily and minute
        tokenBucketService.consumeToken();

        // Proceed with the original method call
        return context.proceed();
    }
}