package com.senai.ml.t1.dota.response;

import java.util.Collections;
import java.util.List;

import com.senai.ml.t1.dota.helper.MathHelper;
import com.senai.ml.t1.dota.models.opendota.Match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {
  private Long matchId;

  private Integer firstBloodTime;

  private List<Integer> radiantGoldAdvantage;

  private List<Integer> radiantExperienceAdvantage;

  private int minRadiantGoldAdvantage;

  private int maxRadiantGoldAdvantage;

  private double meanRadiantGoldAdvantage;

  private double stdDevRadiantGoldAdvantage;

  private int finalRadiantGoldAdvantage;

  public MatchResponse(Match match) {
    this.matchId = match.getMatchId();
    this.firstBloodTime = match.getFirstBloodTime();
    this.radiantGoldAdvantage = match.getRadiantGoldAdvantage();
    this.radiantExperienceAdvantage = match.getRadiantExperienceAdvantage();

    
    this.minRadiantGoldAdvantage = Collections.min(radiantGoldAdvantage);
    this.maxRadiantGoldAdvantage = Collections.max(radiantGoldAdvantage);
    this.meanRadiantGoldAdvantage = radiantGoldAdvantage.stream().mapToInt(Integer::intValue).average().orElse(0);
    this.stdDevRadiantGoldAdvantage = MathHelper.calculateStandardDeviation(radiantGoldAdvantage);
    this.finalRadiantGoldAdvantage = radiantGoldAdvantage.get(radiantGoldAdvantage.size() - 1);
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
