package com.senai.ml.t1.dota.schedulers;

import java.util.LinkedList;
import java.util.Queue;

import com.senai.ml.t1.dota.clients.opendota.OpenDotaClient;
import com.senai.ml.t1.dota.clients.opendota.requestbean.PublicMatchesBean;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;
import com.senai.ml.t1.dota.services.match.MatchService;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;

@Slf4j
@Singleton
public class FetchMatchesScheduler {
  private final OpenDotaClient openDotaClient;

  private final MatchService matchService;

  private Disposable subscription;

  private Queue<OpenDotaMatch> matchQueue = new LinkedList<>();

  public FetchMatchesScheduler(OpenDotaClient openDotaClient, MatchService matchService) {
    this.openDotaClient = openDotaClient;
    this.matchService = matchService;
  }

  @Scheduled(fixedRate = "60s")
  public void getPublicMatches() {
    if (this.subscription != null)
      this.subscription.dispose();
    PublicMatchesBean publicMatchesBean = new PublicMatchesBean();
    publicMatchesBean.setMinRank(80);
    publicMatchesBean.setPage(1);

    log.info("fetching 100 public matches...");

    this.subscription = this.openDotaClient.getPublicMatches(publicMatchesBean)
        .doOnSuccess(matchOverviewList -> {
          matchOverviewList.forEach(match -> {
            if (this.matchService.isMatchAlreadyFetched(match.getMatchId())) {
              log.info("Match with ID {} already fetched", match.getMatchId());
              return;
            }
            OpenDotaMatch matchDetails = this.openDotaClient.getMatchDetails(match.getMatchId()).doOnSuccess();
          });
        })
        .doOnError(error -> log.error("Error fetching match details: " + error.getMessage()))
        .subscribe();
  }

  public void saveMa
}
