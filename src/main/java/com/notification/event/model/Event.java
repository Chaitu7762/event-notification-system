package com.notification.event.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private String eventId;
    private EventType eventType;
    private Object payload;
    private String callbackUrl;
    private Instant createdAt;
}
