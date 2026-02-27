package com.notification.event.processor;

import com.notification.event.model.Event;

public interface EventProcessor {
    void process(Event event);
}


