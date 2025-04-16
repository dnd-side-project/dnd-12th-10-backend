package com.dnd.reevserver.domain.statistics.service;

import com.dnd.reevserver.domain.statistics.dto.response.GetRetrospectStatsResponseDto;
import com.dnd.reevserver.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void writeStatistics(String userId) {
        statisticsRepository.incrementRetrospectCount(userId, LocalDate.now());
    }

    public List<GetRetrospectStatsResponseDto> getStatistics(String userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Map<String, AttributeValue>> rawItems = statisticsRepository.getUserRetrospectStats(userId, startDate, endDate);

        return rawItems.stream()
                .map(item -> new GetRetrospectStatsResponseDto(
                        item.get("date").s(),
                        Integer.parseInt(item.get("cnt").n())
                ))
                .collect(Collectors.toList());
    }
}