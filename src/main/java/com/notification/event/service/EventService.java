package com.notification.event.service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.notification.event.dto.EventRequest;
import com.notification.event.model.Event;
import com.notification.event.queue.EventQueueManager;

@Service
public class EventService {
    private final EventQueueManager queueManager;

    public EventService(EventQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public Event createEvent(EventRequest request) {

        validatePayload(request);

        Event event = Event.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(request.getEventType())
                .payload(request.getPayload())
                .callbackUrl(request.getCallbackUrl())
                .createdAt(Instant.now())
                .build();

        queueManager.submitEvent(event);
        return event;
    }

    private void validatePayload(EventRequest request) {

        Map<String, Object> payload = request.getPayload();

        switch (request.getEventType()) {
            case EMAIL -> {
                if (!payload.containsKey("recipient") || !payload.containsKey("message"))
                    throw new IllegalArgumentException("Invalid EMAIL payload");
            }
            case SMS -> {
                if (!payload.containsKey("phoneNumber") || !payload.containsKey("message"))
                    throw new IllegalArgumentException("Invalid SMS payload");
            }
            case PUSH -> {
                if (!payload.containsKey("deviceId") || !payload.containsKey("message"))
                    throw new IllegalArgumentException("Invalid PUSH payload");
            }
        }
    }

    
}
