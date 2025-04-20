package com.dnd.reevserver.domain.statistics.controller;

import com.dnd.reevserver.domain.statistics.dto.response.GetRetrospectStatsResponseDto;
import com.dnd.reevserver.domain.statistics.dto.response.MonthlyActivityStatsResponseDto;
import com.dnd.reevserver.domain.statistics.dto.response.MonthlyComparisonStatsResponseDto;
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
    public ResponseEntity<List<GetRetrospectStatsResponseDto>> getUserRepoStatistics(@AuthenticationPrincipal String userId,
                                                                             @RequestParam int year,
                                                                             @RequestParam int month) {
        return ResponseEntity.ok(statisticsService.getUserRepoStatistics(userId, year, month));
    }

    @Operation(summary = "모든 유저의 평균 한달 간 회고 작성 수와 해당 유저의 한달 간 회고 작성 수의 비교",
            description = "isBiggerThenAll이 true면 해당 유저가 더 많이 썼다는 것입니다.")
    @GetMapping("/diff")
    public ResponseEntity<MonthlyComparisonStatsResponseDto> getAvgComparison(@AuthenticationPrincipal String userId,
                                                                              @RequestParam int year,
                                                                              @RequestParam int month){
        return ResponseEntity.ok(statisticsService.getAvgComparison(userId, year, month));
    }

    @Operation(summary = "유저의 월간 회고 작성 활동 통계",
            description = "특정 연도와 월에 해당하는 회고 작성 활동에 대한 글자 수, 작성 요일, 태그 통계를 제공합니다.")
    @GetMapping("/activity")
    public ResponseEntity<MonthlyActivityStatsResponseDto> getUserActivityStatistics(@AuthenticationPrincipal String userId,
                                                                                     @RequestParam int year,
                                                                                     @RequestParam int month) {
        return ResponseEntity.ok(statisticsService.getUserRecordStatistics(userId, year, month));
    }
}