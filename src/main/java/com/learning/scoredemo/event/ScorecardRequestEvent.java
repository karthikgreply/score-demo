package com.learning.scoredemo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScorecardRequestEvent {
    private String eventId;
    private String customerId;
    private Instant requestTime;
    private String eventType;
}