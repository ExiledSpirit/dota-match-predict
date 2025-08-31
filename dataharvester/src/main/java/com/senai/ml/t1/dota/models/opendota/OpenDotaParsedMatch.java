package com.senai.ml.t1.dota.models.opendota;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class OpenDotaParsedMatch {
  @JsonProperty("match_id")
  private Long matchId;
}
