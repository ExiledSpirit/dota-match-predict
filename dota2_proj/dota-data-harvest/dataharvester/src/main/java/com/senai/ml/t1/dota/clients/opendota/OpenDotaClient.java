package com.senai.ml.t1.dota.clients.opendota;

import java.util.List;

import com.senai.ml.t1.dota.annotations.RateLimited;
import com.senai.ml.t1.dota.clients.opendota.requestbean.PublicMatchesBean;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatchOverview;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("https://api.opendota.com/api")
public interface OpenDotaClient {
  @RateLimited
  @Get("/matches/{matchId}")
  Mono<OpenDotaMatch> getMatchDetails(@PathVariable long matchId);

  @RateLimited
  @Get("/publicMatches{?less_than_match_id,min_rank,max_rank,mmr_ascending,mmr_descending,page}")
  Mono<List<OpenDotaMatchOverview>> getPublicMatches(@RequestBean PublicMatchesBean publicMatchesBean);
}
