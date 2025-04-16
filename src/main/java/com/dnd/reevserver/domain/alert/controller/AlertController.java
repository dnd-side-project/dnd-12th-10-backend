package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.dto.response.AlertListResponseDto;
import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @Operation(summary = "SQS 대기열(알림) 실시간으로 받는 api")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertMessageResponseDto> streamMessages(@RequestParam String userId) {
        return alertService.getMessageStream(userId); // 실시간 스트리밍
    }

    @Operation(summary = "유저의 알림 목록 및 미읽음 알림 수 조회")
    @GetMapping("/list")
    public ResponseEntity<AlertListResponseDto> getUserAlerts(@RequestParam String userId) {
        return ResponseEntity.ok(alertService.getUserAlertList(userId));
    }
}
