package com.notification.event.processor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.notification.event.model.Event;
import com.notification.event.service.CallbackService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultEventProcessor implements EventProcessor {

    private final CallbackService callbackService;
    private final Random random = new Random();

    public DefaultEventProcessor(CallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @Override
    public void process(Event event) {
        Map<String, Object> callbackBody = new HashMap<>();
        callbackBody.put("eventId", event.getEventId());
        callbackBody.put("eventType", event.getEventType());
        callbackBody.put("processedAt", Instant.now().toString());

        try {
            Thread.sleep(event.getEventType().getProcessingTime());

            // 10% random failure
            if (random.nextInt(100) < 10) {
                throw new RuntimeException("Simulated processing failure");
            }

            callbackBody.put("status", "COMPLETED");
            log.info("Event completed {}", event.getEventId());

        } catch (Exception e) {
            callbackBody.put("status", "FAILED");
            callbackBody.put("errorMessage", e.getMessage());
            log.error("Event failed {}", event.getEventId());
        }
        callbackService.sendCallback(event.getCallbackUrl(), callbackBody);
    }

}
