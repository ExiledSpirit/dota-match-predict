package com.senai.ml.t1.dota.clients.opendota.requestbean;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Introspected
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicMatchesBean {
  private HttpRequest<?> httpRequest;

  @Nullable
  @QueryValue("less_than_match_id")
  private Long lessThanMatchId;

  @Nullable
  @QueryValue("min_rank")
  private Integer minRank;

  @Nullable
  @QueryValue("max_rank")
  private Integer maxRank;

  @Nullable
  @QueryValue("mmr_ascending")
  private Integer mmrAscending;

  @Nullable
  @QueryValue("mmr_descending")
  private Integer mmrDescending;

  @Nullable
  @QueryValue("page")
  private Integer page;
}