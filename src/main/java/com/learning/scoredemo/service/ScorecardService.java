package com.learning.scoredemo.service;

import com.learning.scoredemo.dto.ScorecardRequestDto;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

public interface ScorecardService {
    Mono<SenderResult<String>> requestScorecard(ScorecardRequestDto requestDto);
}