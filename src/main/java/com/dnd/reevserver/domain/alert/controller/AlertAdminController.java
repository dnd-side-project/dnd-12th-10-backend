package com.dnd.reevserver.domain.alert.controller;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.service.AlertAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/alert/failed")
@RequiredArgsConstructor
@Tag(name = "AlertAdmin", description = "실패한 알림 메시지 관리 API")
public class AlertAdminController {

    private final AlertAdminService alertAdminService;

    @GetMapping
    @Operation(summary = "실패한 알림 메시지 목록 조회", description = "Redis에 저장된 실패 메시지 전체를 조회합니다.")
    public ResponseEntity<List<AlertMessageResponseDto>> getFailedMessages() {
        return ResponseEntity.ok(alertAdminService.getFailedMessages());
    }

    @PostMapping("/retry")
    @Operation(summary = "실패한 알림 메시지 재처리", description = "선택한 실패 메시지를 재처리하여 Redis에서 제거합니다.")
    public ResponseEntity<String> retryMessage(@RequestBody String messageJson) {
        try {
            String result = alertAdminService.retryMessage(messageJson);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "실패 메시지 전체 삭제", description = "Redis에 저장된 모든 실패 메시지를 삭제합니다.")
    public ResponseEntity<String> clearFailedMessages() {
        alertAdminService.clearFailedMessages();
        return ResponseEntity.ok("실패 메시지 전부 삭제 완료");
    }
}