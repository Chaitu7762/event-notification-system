package com.notification.event.queue;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.notification.event.model.Event;
import com.notification.event.model.EventType;
import com.notification.event.processor.EventProcessor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventQueueManager {
    private final Map<EventType, BlockingQueue<Event>> queueMap = new ConcurrentHashMap<>();
    private final Map<EventType, ExecutorService> executorMap = new ConcurrentHashMap<>();
    private final EventProcessor processor;

    private volatile boolean acceptingEvents = true;

    public EventQueueManager(EventProcessor processor) {
        this.processor = processor;
    }

    @PostConstruct
    public void init() {
        for (EventType type : EventType.values()) {

            BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
            queueMap.put(type, queue);

            ExecutorService executor = Executors.newSingleThreadExecutor();

            executor.submit(() -> {
                while (acceptingEvents || !queue.isEmpty()) {
                    try {
                        Event event = queue.poll(1, TimeUnit.SECONDS);
                        if (event != null) {
                            processor.process(event);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            executorMap.put(type, executor);
        }
    }

    public void submitEvent(Event event) {
        if (!acceptingEvents) {
            throw new IllegalStateException("System shutting down. Not accepting events.");
        }
        queueMap.get(event.getEventType()).offer(event);
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down gracefully...");
        acceptingEvents = false;

        executorMap.values().forEach(executor -> {
            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        log.info("Shutdown complete.");
    }
}
