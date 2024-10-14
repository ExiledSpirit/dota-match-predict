package com.senai.ml.t1.dota.controller;

import com.senai.ml.t1.dota.services.match.MatchService;
import com.senai.ml.t1.dota.models.entities.MatchEntity;
import java.util.List;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller(value = "/match")
public class MatchController {
  private final MatchService matchService;

  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  @Get()
  public HttpResponse<List<MatchEntity>> fetchMatches() {
    List<MatchEntity> matches = this.matchService.getAll();
    log.info("testing csv printer {}", matches.get(0).printCsv());
    return HttpResponse.ok(matches);
  }

  @Get(produces = MediaType.APPLICATION_OCTET_STREAM, value = "/csv")
  public HttpResponse<byte[]> getMatchesCsv() {
    StringBuilder csvContent = new StringBuilder().append(new MatchEntity().printCsvHeader());
    this.matchService.getAll()
        .forEach(match -> csvContent.append(match.printCsv()));

    String csv = csvContent.toString();

    byte[] csvBytes = csv.getBytes();
    return HttpResponse.ok(csvBytes);
  }
}
