package com.senai.ml.t1.dota.models.opendota;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Serdeable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MatchOverview {
  @JsonProperty("match_id")
  private Long matchId;

  @JsonProperty("radiant_win")
  private Boolean radiantWin;

  @JsonProperty("start_time")
  private Long startTime;

  @JsonProperty("duration")
  private Integer duration;

  @JsonProperty("lobby_type")
  private Integer lobbyType;

  @JsonProperty("game_mode")
  private Integer gameMode;

  @JsonProperty("avg_rank_tier")
  private Integer averageRankTier;

  @JsonProperty("num_rank_tier")
  private Integer numRankTier;

  @JsonProperty("cluster")
  private Integer cluster;

  @JsonProperty("radiant_team")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> radiantTeam;

  @JsonProperty("dire_team")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> direTeam;
}
