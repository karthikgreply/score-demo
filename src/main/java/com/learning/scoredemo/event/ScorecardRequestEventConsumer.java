package com.learning.scoredemo.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class ScorecardRequestEventConsumer {

    private final Sinks.One<ScorecardRequestEvent> lastEventSink = Sinks.one();

    @KafkaListener(topics = "${app.kafka.topic.scorecard-request}")
    public void receiveEvent(ScorecardRequestEvent event) {
        log.info("Received event: {}", event);
        lastEventSink.tryEmitValue(event);
    }

    public Mono<ScorecardRequestEvent> getLatestEvent() {
        return lastEventSink.asMono();
    }
}