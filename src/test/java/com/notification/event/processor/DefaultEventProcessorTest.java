package com.notification.event.processor;
import org.junit.jupiter.api.Test;

import com.notification.event.model.Event;
import com.notification.event.model.EventType;
import com.notification.event.service.CallbackService;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultEventProcessorTest {
     @Test
    void processor_shouldSendCallback() {

        CallbackService callbackService = mock(CallbackService.class);
        DefaultEventProcessor processor = new DefaultEventProcessor(callbackService);

        Event event = Event.builder()
                .eventId("123")
                .eventType(EventType.PUSH)
                .payload(Map.of())
                .callbackUrl("http://test.com")
                .createdAt(Instant.now())
                .build();

        processor.process(event);

        verify(callbackService, atLeastOnce())
                .sendCallback(eq("http://test.com"), any());
    }
}
