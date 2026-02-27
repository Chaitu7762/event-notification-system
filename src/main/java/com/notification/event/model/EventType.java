package com.notification.event.model;

public enum EventType {
    EMAIL(5000),
    SMS(3000),
    PUSH(2000);

    private final long processingTime;

    EventType(long processingTime) {
        this.processingTime = processingTime;
    }

    public long getProcessingTime() {
        return processingTime;
    }
}
