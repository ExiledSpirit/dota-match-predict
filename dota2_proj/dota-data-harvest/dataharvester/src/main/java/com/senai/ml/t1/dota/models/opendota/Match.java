package com.senai.ml.t1.dota.models.opendota;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Match implements Serializable {
  @JsonProperty(value = "first_blood_time")
  private String firstBloodTime;

  public Match() {
  }

  public Match(String firstBloodTime) {
    this.firstBloodTime = firstBloodTime;
  }

  public String getFirstBloodTime() {
    return firstBloodTime;
  }

  public void setFirstBloodTime(String firstBloodTime) {
    this.firstBloodTime = firstBloodTime;
  }

}
