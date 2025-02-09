package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @Operation(summary = "SSE 연결 요청")
    @GetMapping("/connect")
    public SseEmitter connect(@AuthenticationPrincipal String userId) {
        return sseService.connect(userId);
    }
}
