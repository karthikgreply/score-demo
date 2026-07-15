package com.learning.scoredemo.service;

import com.learning.scoredemo.dto.ScorecardRequestDto;
import com.learning.scoredemo.event.ScorecardRequestEvent;
import com.learning.scoredemo.event.ScorecardRequestEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScorecardServiceImpl implements ScorecardService {

    private final ScorecardRequestEventPublisher publisher;

    @Override
    public Mono<Void> requestScorecard(ScorecardRequestDto requestDto) {
        ScorecardRequestEvent event = ScorecardRequestEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .customerId(requestDto.getCustomerId())
                .requestTime(Instant.now())
                .eventType("SCORECARD_EVALUATION_REQUESTED")
                .build();

        return publisher.publish(event);
    }
}