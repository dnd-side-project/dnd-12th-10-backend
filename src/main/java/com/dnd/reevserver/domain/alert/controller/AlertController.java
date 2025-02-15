package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamMessages() {
        return alertService.getMessageStream();
    }
}
