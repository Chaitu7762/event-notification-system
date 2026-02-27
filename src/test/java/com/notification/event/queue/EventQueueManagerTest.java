package com.notification.event.queue;

import org.junit.jupiter.api.Test;

import com.notification.event.model.Event;
import com.notification.event.model.EventType;
import com.notification.event.processor.EventProcessor;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
public class EventQueueManagerTest {
     @Test
    void eventsShouldBeProcessedInFIFO() throws InterruptedException {

        EventProcessor processor = mock(EventProcessor.class);
        EventQueueManager manager = new EventQueueManager(processor);
        manager.init();

        Event event1 = Event.builder()
                .eventId("1")
                .eventType(EventType.PUSH)
                .createdAt(Instant.now())
                .build();

        Event event2 = Event.builder()
                .eventId("2")
                .eventType(EventType.PUSH)
                .createdAt(Instant.now())
                .build();

        manager.submitEvent(event1);
        manager.submitEvent(event2);

        Thread.sleep(3000);

        verify(processor).process(event1);
        verify(processor).process(event2);
    }

    @Test
    void shutdown_shouldStopAcceptingEvents() {

        EventProcessor processor = mock(EventProcessor.class);
        EventQueueManager manager = new EventQueueManager(processor);
        manager.init();
        manager.shutdown();

        Event event = Event.builder()
                .eventId("1")
                .eventType(EventType.EMAIL)
                .createdAt(Instant.now())
                .build();

        assertThrows(IllegalStateException.class,
                () -> manager.submitEvent(event));
    }
}
