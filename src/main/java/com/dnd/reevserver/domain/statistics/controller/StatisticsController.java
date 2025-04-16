package com.dnd.reevserver.domain.statistics.controller;

import com.dnd.reevserver.domain.statistics.dto.request.DateRequestDto;
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
@Tag(name = "Statistics", description = "회고 통계 API")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "월별 회고 통계 조회", description = "특정 월에 유저가 며칠에 몇 건의 회고를 작성했는지 통계를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<List<GetRetrospectStatsResponseDto>> getStatistics(@AuthenticationPrincipal String userId, @RequestBody DateRequestDto requestDto) {
        return ResponseEntity.ok(statisticsService.getStatistics(userId, requestDto));
    }
}