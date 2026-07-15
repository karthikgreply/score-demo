package com.learning.scoredemo.service;

import com.learning.scoredemo.dto.ScorecardRequestDto;
import reactor.core.publisher.Mono;

public interface ScorecardService {
    Mono<Void> requestScorecard(ScorecardRequestDto requestDto);
}