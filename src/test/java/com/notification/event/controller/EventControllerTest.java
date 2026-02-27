package com.notification.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.event.dto.EventRequest;
import com.notification.event.model.Event;
import com.notification.event.model.EventType;
import com.notification.event.service.EventService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

@WebMvcTest
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validEventSubmission_shouldReturn200() throws Exception {

        Event mockEvent = Event.builder()
                .eventId("123")
                .build();

        when(eventService.createEvent(any()))
                .thenReturn(mockEvent);

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);
        request.setPayload(Map.of("recipient", "test@test.com", "message", "hello"));
        request.setCallbackUrl("http://localhost/test");

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void invalidEventType_shouldReturn400() throws Exception {

        String body = """
                {
                  "eventType":"INVALID",
                  "payload":{},
                  "callbackUrl":"http://test.com"
                }
                """;

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }
}