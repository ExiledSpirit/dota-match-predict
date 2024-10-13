package com.senai.ml.t1.dota.models.entities;

import java.util.Collections;
import java.util.List;

import com.senai.ml.t1.dota.helper.MathHelper;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Serdeable
@MappedEntity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "match")
public class MatchEntity {
  @Id
  private Long id;

  @Nullable
  private Integer firstBloodTime;

  @Nullable
  private List<Integer> radiantGoldAdvantage;

  @Nullable
  private List<Integer> radiantExperienceAdvantage;

  @Nullable
  private Boolean radiantWin;

  @Nullable
  private int minRadiantGoldAdvantage;

  @Nullable
  private int maxRadiantGoldAdvantage;

  @Nullable
  private double meanRadiantGoldAdvantage;

  @Nullable
  private double stdDevRadiantGoldAdvantage;

  @Nullable
  private int finalRadiantGoldAdvantage;

  @Nullable
  private int minRadiantExperienceAdvantage;

  @Nullable
  private int maxRadiantExperienceAdvantage;

  @Nullable
  private double meanRadiantExperienceAdvantage;

  @Nullable
  private double stdDevRadiantExperienceAdvantage;

  @Nullable
  private int finalRadiantExperienceAdvantage;

  public MatchEntity(OpenDotaMatch match) {
    this.id = match.getMatchId();
    this.firstBloodTime = match.getFirstBloodTime();
    this.radiantGoldAdvantage = match.getRadiantGoldAdvantage();
    this.radiantExperienceAdvantage = match.getRadiantExperienceAdvantage();
    this.radiantWin = match.getRadiantWin();

    if (radiantGoldAdvantage != null && !radiantGoldAdvantage.isEmpty()) {
      this.minRadiantGoldAdvantage = Collections.min(radiantGoldAdvantage);
      this.maxRadiantGoldAdvantage = Collections.max(radiantGoldAdvantage);
      this.meanRadiantGoldAdvantage = radiantGoldAdvantage.stream().mapToInt(Integer::intValue).average().orElse(0);
      this.stdDevRadiantGoldAdvantage = MathHelper.calculateStandardDeviation(radiantGoldAdvantage);
      this.finalRadiantGoldAdvantage = radiantGoldAdvantage.get(radiantGoldAdvantage.size() - 1);
    }

    if (radiantExperienceAdvantage != null && !radiantExperienceAdvantage.isEmpty()) {
      this.minRadiantExperienceAdvantage = Collections.min(radiantExperienceAdvantage);
      this.maxRadiantExperienceAdvantage = Collections.max(radiantExperienceAdvantage);
      this.meanRadiantExperienceAdvantage = radiantExperienceAdvantage.stream().mapToInt(Integer::intValue).average()
          .orElse(0);
      this.stdDevRadiantExperienceAdvantage = MathHelper.calculateStandardDeviation(radiantExperienceAdvantage);
      this.finalRadiantExperienceAdvantage = radiantExperienceAdvantage.get(radiantExperienceAdvantage.size() - 1);
    }
  }
}
