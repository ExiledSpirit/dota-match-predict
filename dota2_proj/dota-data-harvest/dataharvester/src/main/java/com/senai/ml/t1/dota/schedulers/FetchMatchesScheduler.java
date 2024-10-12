package com.senai.ml.t1.dota.schedulers;

import com.senai.ml.t1.dota.clients.opendota.OpenDotaClient;
import com.senai.ml.t1.dota.clients.opendota.requestbean.PublicMatchesBean;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;
import com.senai.ml.t1.dota.services.match.MatchService;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Slf4j
@Singleton
public class FetchMatchesScheduler {
  private final OpenDotaClient openDotaClient;
  private final MatchService matchService;
  private Disposable subscription;

  public FetchMatchesScheduler(OpenDotaClient openDotaClient, MatchService matchService) {
    this.openDotaClient = openDotaClient;
    this.matchService = matchService;
  }

  @Scheduled(fixedRate = "60s")
  public void getPublicMatches() {
    // Dispose previous subscription if it exists
    if (subscription != null && !subscription.isDisposed()) {
      subscription.dispose();
    }

    log.info("Fetching 100 public matches...");

    PublicMatchesBean publicMatchesBean = new PublicMatchesBean();
    publicMatchesBean.setMinRank(80);
    publicMatchesBean.setPage(1);

    // Fetch the public matches and process them
    subscription = openDotaClient.getPublicMatches(publicMatchesBean)
        .flatMapMany(Flux::fromIterable) // Convert List<Match> to Flux<Match>
        .filter(match -> !matchService.isMatchAlreadyFetched(match.getMatchId()))
        .doOnNext(match -> log.info("Fetching match details of ID: {}", match.getMatchId()))
        .concatMap(match -> openDotaClient.getMatchDetails(match.getMatchId())
            .doOnNext(this::saveMatch)
            .doOnError(error -> log.error("Error fetching match details for ID {}: {}", match.getMatchId(),
                error.getMessage())))
        .doOnError(error -> log.error("Error fetching public matches: {}", error.getMessage()))
        .subscribe();
  }

  private void saveMatch(OpenDotaMatch openDotaMatch) {
    try {
      matchService.saveMatch(openDotaMatch);
      log.info("Match with ID {} saved successfully.", openDotaMatch.getMatchId());
    } catch (Exception ex) {
      log.error("Error saving match with ID {}: {}", openDotaMatch.getMatchId(), ex.getMessage(), ex);
    }
  }
}