package com.notification.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponse {
    private String eventId;
    private String message;
}
