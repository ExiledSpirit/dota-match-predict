package com.senai.ml.t1.dota.repository.match;

import com.senai.ml.t1.dota.models.entities.MatchEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface MatchRepository extends PageableRepository<MatchEntity, Long> {
}
