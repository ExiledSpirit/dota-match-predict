package com.senai.ml.t1.dota.services.match;

import java.util.List;
import java.util.Optional;

import com.senai.ml.t1.dota.models.entities.MatchEntity;
import com.senai.ml.t1.dota.models.opendota.OpenDotaMatch;
import com.senai.ml.t1.dota.repository.match.MatchRepository;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MatchService {
  private final MatchRepository matchRepository;

  public MatchService(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  public boolean isMatchAlreadyFetched(Long matchId) {
    Optional<MatchEntity> matchOptional = matchRepository.findById(matchId);

    return matchOptional.isPresent();
  }

  public MatchEntity saveMatch(OpenDotaMatch openDotaMatch) {
    log.info("Saving match to database");
    MatchEntity newMatch = new MatchEntity(openDotaMatch);

    return this.matchRepository.save(newMatch);
  }

  public List<MatchEntity> saveAllMatches(List<OpenDotaMatch> matches) {
    return this.matchRepository.saveAll(matches.stream().map(MatchEntity::new).toList());
  }

  public List<MatchEntity> getAll() {
    return this.matchRepository.findAll();
  }
}
