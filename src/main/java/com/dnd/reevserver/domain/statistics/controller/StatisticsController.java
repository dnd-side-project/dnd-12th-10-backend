package com.dnd.reevserver.domain.statistics.controller;

import com.dnd.reevserver.domain.statistics.dto.response.GetRetrospectStatsResponseDto;
import com.dnd.reevserver.domain.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "통계 API")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "월별 회고 통계 조회", description = "특정 연도와 월에 해당하는 회고 작성 통계를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<List<GetRetrospectStatsResponseDto>> getStatistics(@AuthenticationPrincipal String userId,
                                                                             @RequestParam int year,
                                                                             @RequestParam int month) {
        return ResponseEntity.ok(statisticsService.getStatistics(userId, year, month));
    }
}