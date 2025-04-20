package com.dnd.reevserver.domain.statistics.service;

import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.statistics.dto.response.GetRetrospectStatsResponseDto;
import com.dnd.reevserver.domain.statistics.dto.response.MonthlyActivityStatsResponseDto;
import com.dnd.reevserver.domain.statistics.dto.response.MonthlyComparisonStatsResponseDto;
import com.dnd.reevserver.domain.statistics.repository.StatisticsRedisRepository;
import com.dnd.reevserver.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final MemberRepository memberRepository;
    private final StatisticsRedisRepository statisticsRedisRepository;
    private final RetrospectRepository retrospectRepository;

    // 매월 1일 유저 수 업데이트 (단일 키에 덮어씀)
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateMonthlyUserCount() {
        long userCount = memberRepository.count();
        statisticsRedisRepository.setCurrentUserCount(userCount);
    }

    // 유저의 한 달 간 회고 작성 수와 전체 유저의 한 달 간 회고 작성 수의 비교
    public MonthlyComparisonStatsResponseDto getAvgComparison(String userId, int year, int month) {
        int userTotal = statisticsRedisRepository.getUserRepoCount(userId, year, month);
        int allTotal = statisticsRedisRepository.getTotalRepoCount(year, month);
        int userCount = statisticsRedisRepository.getCurrentUserCount();

        int overallAvg = userCount == 0 ? 0 : (allTotal / userCount); // 소수점 버림
        int difference = userTotal - overallAvg;

        return new MonthlyComparisonStatsResponseDto(userId, userTotal, overallAvg, difference, difference > 0);
    }

    // 회고 작성 유저의 당일 회고 작성 수와 유저의 한 달 간 작성 회고 수 총합 증가
    public void writeUserRepoStatistics(String userId) {
        LocalDate now = LocalDate.now();
        statisticsRepository.incrementRetrospectCount(userId, now);
        statisticsRedisRepository.incrementUserRepoCount(userId, now.getYear(), now.getMonthValue());
    }

    // 유저의 한 달 간 회고 작성 수 반환
    public List<GetRetrospectStatsResponseDto> getUserRepoStatistics(String userId, int year, int month) {
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

    // 유저의 월 간 기록 반환
    public MonthlyActivityStatsResponseDto getUserRecordStatistics(String userId, int year, int month) {
        String yearMonth = String.format("%04d-%02d", year, month);
        // 총 작성한 글자 수
        int totalCharacters = retrospectRepository.getTotalWrittenCharacters(userId, yearMonth).orElse(0);
        // 이번 달 작성 회고 수
        int retrospectCount = statisticsRedisRepository.getUserRepoCount(userId, year, month);
        // 주로 작성한 회고일
        int dow = retrospectRepository.getMostFrequentWritingDay(userId, yearMonth) - 1;
        String mostFrequentDayOfWeek = DayOfWeek.of(dow).getDisplayName(TextStyle.FULL, Locale.KOREAN);
        // 많이 사용한 태그
        List<String> mostUsedTags = List.of(); // TODO: RetrospectCategory 완성 후 구현

        return new MonthlyActivityStatsResponseDto(
                userId,
                totalCharacters,
                retrospectCount,
                mostFrequentDayOfWeek,
                mostUsedTags
        );
    }
}