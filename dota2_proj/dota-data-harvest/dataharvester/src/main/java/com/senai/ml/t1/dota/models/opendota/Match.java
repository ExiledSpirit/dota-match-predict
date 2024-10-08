package com.senai.ml.t1.dota.models.opendota;

import java.io.Serializable;
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

@Data
@Serdeable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match implements Serializable {
  @JsonProperty(value = "match_id")
  private Long matchId;

  @JsonProperty(value = "first_blood_time")
  private Integer firstBloodTime;

  @JsonProperty(value = "radiant_gold_adv")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> radiantGoldAdvantage;

  @JsonProperty(value = "radiant_xp_adv")
  @JsonDeserialize(as = ArrayList.class)
  private List<Integer> radiantExperienceAdvantage;

  @Override
  public String toString() {
    return "Match [matchId=" + matchId + ", \n firstBloodTime=" + firstBloodTime + ", \n radiantGoldAdvantage="
        + radiantGoldAdvantage + ", \n radiantExperienceAdvantage=" + radiantExperienceAdvantage
        + "]";
  }
}
