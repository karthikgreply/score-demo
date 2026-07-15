package com.learning.scoredemo.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScorecardRequestEventPublisher {

    private final KafkaSender<String, Object> kafkaSender;

    @Value("${app.kafka.topic.scorecard-request}")
    private String topicName;

    public Mono<Void> publish(ScorecardRequestEvent event) {
        String key = event.getCustomerId(); // Use customerId as routing key

        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topicName, key, event);
        SenderRecord<String, Object, String> senderRecord = SenderRecord.create(producerRecord, event.getEventId());

        return kafkaSender.send(Mono.just(senderRecord))
                .doOnNext(result -> log.info(":::Successfully published event ID {} to topic {} at offset {}",
                        result.correlationMetadata(),
                        result.recordMetadata().topic(),
                        result.recordMetadata().offset()))
                .doOnError(error -> log.error("Error publishing event ID {}", event.getEventId(), error))
                .then();
    }
}