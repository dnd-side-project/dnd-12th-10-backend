package com.dnd.reevserver.domain.statistics.dto.response;

import java.util.List;

public record MonthlyActivityStatsResponseDto(
        String userId,
        int totalCharacters,
        int retrospectCount,
        String mostFrequentDayOfWeek,
        List<String> mostUsedTags
) {}

