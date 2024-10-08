package com.senai.ml.t1.dota.clients;

import com.senai.ml.t1.dota.models.opendota.Match;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("https://api.opendota.com/api")
public interface OpenDotaClient {
  @Get("/matches/{matchId}")
  Mono<Match> getMatchDetails(@PathVariable long matchId);
}
