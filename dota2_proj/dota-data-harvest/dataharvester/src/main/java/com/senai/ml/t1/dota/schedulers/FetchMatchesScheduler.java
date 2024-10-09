package com.senai.ml.t1.dota.schedulers;

import com.senai.ml.t1.dota.clients.opendota.OpenDotaClient;
import com.senai.ml.t1.dota.clients.opendota.requestbean.PublicMatchesBean;
import com.senai.ml.t1.dota.response.MatchResponse;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import reactor.core.Disposable;

@Singleton
public class FetchMatchesScheduler {
  private final OpenDotaClient openDotaClient;

  private Disposable subscription;

  public FetchMatchesScheduler(OpenDotaClient openDotaClient) {
    this.openDotaClient = openDotaClient;
  }

  @Scheduled(fixedRate = "60s")
  public void getPublicMatches() {
    if (this.subscription != null)
      this.subscription.dispose();
    PublicMatchesBean publicMatchesBean = new PublicMatchesBean();
    publicMatchesBean.setMinRank(80);
    publicMatchesBean.setPage(1);

    this.subscription = this.openDotaClient.getPublicMatches(publicMatchesBean)
        .doOnSuccess(matchOverviewList -> {
          System.out.println(matchOverviewList.size());
          System.out.println(matchOverviewList.get(0));
        })
        .doOnError(error -> System.err.println("Error fetching match details: " + error.getMessage()))
        .subscribe();
  }
}
