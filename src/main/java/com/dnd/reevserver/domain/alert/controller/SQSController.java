package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.service.SQSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alert")
public class SQSController {
    private final SQSService sqsService;

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        sqsService.sendMessage(message);
    }
}
