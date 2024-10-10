package com.senai.ml.t1.dota.services.match;

import java.util.List;

import com.senai.ml.t1.dota.models.opendota.Match;

import jakarta.inject.Singleton;

@Singleton
public class MatchServicePostgresImpl implements MatchService {
  @Override
  public boolean isMatchAlreadyFetched(Long matchId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isMatchAlreadyFetched'");
  }

  @Override
  public boolean saveMatch(Match match) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveMatch'");
  }

  @Override
  public boolean saveAllMatches(List<Match> matches) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveAllMatches'");
  }
}
