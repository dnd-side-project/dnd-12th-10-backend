package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.dto.response.AlertResponseDto;
import com.dnd.reevserver.domain.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @Operation(summary = "모든 알림 조회")
    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> getAlerts(@AuthenticationPrincipal String userId) {
        List<AlertResponseDto> response = alertService.getAlerts(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "안읽은 알림 개수 조회")
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadAlertCount(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(alertService.countUnreadAlerts(userId));
    }

    @Operation(summary = "특정 알림 읽음 처리")
    @PatchMapping("/{alertId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long alertId) {
        alertService.markAlertAsRead(alertId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 알림 읽음 처리")
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal String userId) {
        alertService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}