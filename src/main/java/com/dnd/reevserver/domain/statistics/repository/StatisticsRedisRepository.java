package com.dnd.reevserver.domain.statistics.repository;

import com.dnd.reevserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StatisticsRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final MemberRepository memberRepository;

    private String getMonthKey(int year, int month) {
        return String.format("%04d-%02d", year, month);
    }

    public void setCurrentUserCount(long userCount) {
        redisTemplate.opsForValue().set("stats:userCount", String.valueOf(userCount));
    }

    public void incrementUserRepoCount(String userId, int year, int month) {
        String suffix = getMonthKey(year, month);
        String userKey = "stats:retrospect:user:" + userId + ":" + suffix;
        String totalKey = "stats:retrospect:total:" + suffix;

        redisTemplate.opsForValue().increment(userKey);
        redisTemplate.opsForValue().increment(totalKey);
    }

    public int getUserRepoCount(String userId, int year, int month) {
        String key = "stats:retrospect:user:" + userId + ":" + getMonthKey(year, month);
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0 : Integer.parseInt(value);
    }

    public int getTotalRepoCount(int year, int month) {
        String key = "stats:retrospect:total:" + getMonthKey(year, month);
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0 : Integer.parseInt(value);
    }

    public int getCurrentUserCount() {
        String value = redisTemplate.opsForValue().get("stats:userCount");

        if (value == null) {
            long countFromDb = memberRepository.count();
            redisTemplate.opsForValue().set("stats:userCount", String.valueOf(countFromDb));
            return (int) countFromDb;
        }

        return Integer.parseInt(value); // 0 방지용 1
    }
}
