package com.senai.ml.t1.dota.models.opendota;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senai.ml.t1.dota.helper.MathHelper;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Match implements Serializable {
  @JsonProperty(value = "match_id")
  @JsonDeserialize(as = Long.class)
  private Long matchId;

  @JsonProperty(value = "first_blood_time")
  private Integer firstBloodTime;

  @JsonProperty(value = "radiant_gold_adv")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> radiantGoldAdvantage;

  @JsonProperty(value = "radiant_xp_adv")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> radiantExperienceAdvantage;

  private int minRadiantGoldAdvantage;

  private int maxRadiantGoldAdvantage;

  private double meanRadiantGoldAdvantage;

  private double stdDevRadiantGoldAdvantage;

  private int finalRadiantGoldAdvantage;

  public Match() {
  }

  public Match(Long matchId, Integer firstBloodTime, List<Integer> radiantGoldAdvantage,
      List<Integer> radiantExperienceAdvantage) {
    this.matchId = matchId;
    this.firstBloodTime = firstBloodTime;
    this.radiantGoldAdvantage = radiantGoldAdvantage;
    this.radiantExperienceAdvantage = radiantExperienceAdvantage;

    this.minRadiantGoldAdvantage = Collections.min(radiantGoldAdvantage);
    this.maxRadiantGoldAdvantage = Collections.max(radiantGoldAdvantage);
    this.meanRadiantGoldAdvantage = radiantGoldAdvantage.stream().mapToInt(Integer::intValue).average().orElse(0);
    this.stdDevRadiantGoldAdvantage = MathHelper.calculateStandardDeviation(radiantGoldAdvantage);
    this.finalRadiantGoldAdvantage = radiantGoldAdvantage.get(radiantGoldAdvantage.size() - 1);
  }

  public int getMinRadiantGoldAdvantage() {
    return minRadiantGoldAdvantage;
  }

  public void setMinRadiantGoldAdvantage(int minRadiantGoldAdvantage) {
    this.minRadiantGoldAdvantage = minRadiantGoldAdvantage;
  }

  public int getMaxRadiantGoldAdvantage() {
    return maxRadiantGoldAdvantage;
  }

  public void setMaxRadiantGoldAdvantage(int maxRadiantGoldAdvantage) {
    this.maxRadiantGoldAdvantage = maxRadiantGoldAdvantage;
  }

  public double getMeanRadiantGoldAdvantage() {
    return meanRadiantGoldAdvantage;
  }

  public void setMeanRadiantGoldAdvantage(double meanRadiantGoldAdvantage) {
    this.meanRadiantGoldAdvantage = meanRadiantGoldAdvantage;
  }

  public double getStdDevRadiantGoldAdvantage() {
    return stdDevRadiantGoldAdvantage;
  }

  public void setStdDevRadiantGoldAdvantage(double stdDevRadiantGoldAdvantage) {
    this.stdDevRadiantGoldAdvantage = stdDevRadiantGoldAdvantage;
  }

  public int getFinalRadiantGoldAdvantage() {
    return finalRadiantGoldAdvantage;
  }

  public void setFinalRadiantGoldAdvantage(int finalRadiantGoldAdvantage) {
    this.finalRadiantGoldAdvantage = finalRadiantGoldAdvantage;
  }

  public Integer getFirstBloodTime() {
    return firstBloodTime;
  }

  public void setFirstBloodTime(Integer firstBloodTime) {
    this.firstBloodTime = firstBloodTime;
  }

  @Override
  public String toString() {
    return "Match [matchId=" + matchId + ", \n firstBloodTime=" + firstBloodTime + ", \n radiantGoldAdvantage="
        + radiantGoldAdvantage + ", \n radiantExperienceAdvantage=" + radiantExperienceAdvantage
        + ", \n minRadiantGoldAdvantage=" + minRadiantGoldAdvantage + ", \nmaxRadiantGoldAdvantage="
        + maxRadiantGoldAdvantage + ", \n meanRadiantGoldAdvantage=" + meanRadiantGoldAdvantage
        + ", \n stdDevRadiantGoldAdvantage=" + stdDevRadiantGoldAdvantage + ", \n finalRadiantGoldAdvantage="
        + finalRadiantGoldAdvantage + "]";
  }
}
