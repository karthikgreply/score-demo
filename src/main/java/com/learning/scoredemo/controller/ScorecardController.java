package com.learning.scoredemo.controller;

import com.learning.scoredemo.dto.ScorecardRequestDto;
import com.learning.scoredemo.event.ScorecardRequestEvent;
import com.learning.scoredemo.event.ScorecardRequestEventConsumer;
import com.learning.scoredemo.service.ScorecardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/scorecards")
@RequiredArgsConstructor
public class ScorecardController {

    private final ScorecardService scorecardService;
    private final ScorecardRequestEventConsumer eventConsumer;

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> requestScorecard(@RequestBody ScorecardRequestDto requestDto) {
        return scorecardService.requestScorecard(requestDto).then();
    }

    @GetMapping("/latest")
    public Mono<ResponseEntity<ScorecardRequestEvent>> getLatestEvent() {
        return eventConsumer.getLatestEvent()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}