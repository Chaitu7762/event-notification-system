package com.notification.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.event.dto.EventRequest;
import com.notification.event.dto.EventResponse;
import com.notification.event.model.Event;
import com.notification.event.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> submit(@Valid @RequestBody EventRequest request) {

        Event event = eventService.createEvent(request);

        return ResponseEntity.ok(
                EventResponse.builder()
                        .eventId(event.getEventId())
                        .message("Event accepted for processing.")
                        .build());
    }
}
