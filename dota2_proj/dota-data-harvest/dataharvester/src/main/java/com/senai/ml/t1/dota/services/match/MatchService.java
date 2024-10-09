package com.senai.ml.t1.dota.services.match;

import java.util.List;

import com.senai.ml.t1.dota.models.opendota.Match;

public interface MatchService {
  public boolean isMatchAlreadyFetched(Long matchId);

  public boolean saveMatch(Match match);

  public boolean saveAllMatches(List<Match> matches);
}
