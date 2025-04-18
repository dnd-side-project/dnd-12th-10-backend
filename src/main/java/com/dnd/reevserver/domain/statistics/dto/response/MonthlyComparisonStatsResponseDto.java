package com.dnd.reevserver.domain.statistics.dto.response;

public record MonthlyComparisonStatsResponseDto(
        String userId,
        int userTotal,
        double overallAverage,
        double difference,
        boolean isBiggerThenAll
) {}

