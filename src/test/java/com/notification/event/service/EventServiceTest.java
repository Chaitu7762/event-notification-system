package com.notification.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.notification.event.dto.EventRequest;
import com.notification.event.model.EventType;
import com.notification.event.queue.EventQueueManager;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventQueueManager queueManager;
    private EventService eventService;

    @BeforeEach
    void setup() {
        queueManager = mock(EventQueueManager.class);
        eventService = new EventService(queueManager);
    }

    @Test
    void validEmailEvent_shouldSubmitToQueue() {

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient","a","message","b"));
        request.setCallbackUrl("http://test.com");

        eventService.createEvent(request);

        verify(queueManager, times(1)).submitEvent(any());
    }

    @Test
    void invalidPayload_shouldThrowException() {

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient","a")); // missing message
        request.setCallbackUrl("http://test.com");

        assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent(request));
    }
}