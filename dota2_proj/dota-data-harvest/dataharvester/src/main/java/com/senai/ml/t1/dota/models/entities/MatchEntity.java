package com.senai.ml.t1.dota.models.entities;

import java.util.Collections;
import java.util.List;

import com.senai.ml.t1.dota.helper.MathHelper;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Serdeable
@MappedEntity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchEntity {
  @Id
  private Long matchId;

  @Nullable
  private Integer firstBloodTime;

  @NotNull
  private List<Integer> radiantGoldAdvantage;

  @NotNull
  private List<Integer> radiantExperienceAdvantage;

  @NotNull
  private Boolean radiantWin;

  @NotNull
  private int minRadiantGoldAdvantage;

  @NotNull
  private int maxRadiantGoldAdvantage;

  @NotNull
  private double meanRadiantGoldAdvantage;

  @NotNull
  private double stdDevRadiantGoldAdvantage;

  @NotNull
  private int finalRadiantGoldAdvantage;

  public MatchEntity(OpenDotaMatch match) {
    this.matchId = match.getMatchId();
    this.firstBloodTime = match.getFirstBloodTime();
    this.radiantGoldAdvantage = match.getRadiantGoldAdvantage();
    this.radiantExperienceAdvantage = match.getRadiantExperienceAdvantage();
    this.radiantWin = match.getRadiantWin();

    this.minRadiantGoldAdvantage = Collections.min(radiantGoldAdvantage);
    this.maxRadiantGoldAdvantage = Collections.max(radiantGoldAdvantage);
    this.meanRadiantGoldAdvantage = radiantGoldAdvantage.stream().mapToInt(Integer::intValue).average().orElse(0);
    this.stdDevRadiantGoldAdvantage = MathHelper.calculateStandardDeviation(radiantGoldAdvantage);
    this.finalRadiantGoldAdvantage = radiantGoldAdvantage.get(radiantGoldAdvantage.size() - 1);
  }
}
