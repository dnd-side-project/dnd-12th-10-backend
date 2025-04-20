package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.dto.response.AlertListResponseDto;
import com.dnd.reevserver.domain.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
@Tag(name = "Alert", description = "알람 관련 API")
public class AlertController {

    private final AlertService alertService;

    @Operation(summary = "유저의 알림 목록 및 미읽음 수 조회 (페이징)")
    @GetMapping("/list")
    public ResponseEntity<AlertListResponseDto> getUserAlerts(
            @AuthenticationPrincipal String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "false") boolean onlyUnread) {
        return ResponseEntity.ok(alertService.getUserAlertList(userId, page, size, onlyUnread));
    }

    @Operation(summary = "알림 읽음 처리")
    @PutMapping("/read")
    public ResponseEntity<Void> updateRead(@AuthenticationPrincipal String userId,
                                           @RequestParam String messageId) {
        alertService.updateRead(userId, messageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "단일 알림 삭제")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteAlert(@AuthenticationPrincipal String userId,
                                            @PathVariable String messageId) {
        alertService.deleteAlert(userId, messageId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 알림 삭제")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllAlerts(@AuthenticationPrincipal String userId) {
        alertService.deleteAllAlerts(userId);
        return ResponseEntity.ok().build();
    }
}