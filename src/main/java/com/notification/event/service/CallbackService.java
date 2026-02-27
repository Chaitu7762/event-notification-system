package com.notification.event.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CallbackService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendCallback(String url, Map<String, Object> body) {
        try {
            restTemplate.postForEntity(url, body, String.class);
            log.info("Callback sent to {}", url);
        } catch (Exception e) {
            log.error("Callback failed: {}", e.getMessage());
        }
    }
}
