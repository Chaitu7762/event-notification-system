package com.notification.event.dto;

import com.notification.event.model.EventType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;
import lombok.Data;

@Data
public class EventRequest {

    @NotNull
    private EventType eventType;

    @NotNull
    private Map<String, Object> payload;

    @NotBlank
    @Pattern(regexp = "https?://.*", message = "Invalid callback URL")
    private String callbackUrl;
}
