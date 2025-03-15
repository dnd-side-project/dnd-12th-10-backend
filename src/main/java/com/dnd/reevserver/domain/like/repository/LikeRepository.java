package com.dnd.reevserver.domain.like.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class LikeRepository {
    @Qualifier("redisBooleanTemplate") // 특정 RedisTemplate을 지정
    private final RedisTemplate<String, Boolean> redisTemplate;

    @Qualifier("redisIntegerTemplate")
    private final RedisTemplate<String, Integer> redisCountTemplate; // 좋아요 개수 저장을 위한 RedisTemplate

    private static final String TOGGLE_LIKE_SCRIPT =
            "if redis.call('EXISTS', KEYS[1]) == 1 then " +
                    "   redis.call('DEL', KEYS[1]); " +  // 좋아요 취소
                    "   redis.call('DECR', KEYS[2]); " + // 좋아요 개수 감소
                    "   return 0; " +
                    "else " +
                    "   redis.call('SET', KEYS[1], '1'); " + // 좋아요 추가
                    "   redis.call('INCR', KEYS[2]); " + // 좋아요 개수 증가
                    "   return 1; " +
                    "end";

    public LikeRepository(RedisTemplate<String, Boolean> redisTemplate,
                          RedisTemplate<String, Integer> redisCountTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisCountTemplate = redisCountTemplate;
    }

    // 유저가 특정 회고에 좋아요를 눌렀는지 여부를 저장하는 키
    private String getLikeKey(String userId, Long retrospectId) {
        return "like:" + userId + ":" + retrospectId;
    }

    // 특정 회고의 총 좋아요 개수를 저장하는 키
    private String getLikeCountKey(Long retrospectId) {
        return "like:count:" + retrospectId;
    }

    /**
     * 좋아요 토글 (Lua 스크립트를 이용해 원자적 연산)
     */
    public boolean toggleLike(String userId, Long retrospectId) {
        String likeKey = getLikeKey(userId, retrospectId);
        String countKey = getLikeCountKey(retrospectId);

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(TOGGLE_LIKE_SCRIPT, Long.class),
                Arrays.asList(likeKey, countKey)
        );

        return result == 1;// 1이면 좋아요 추가, 0이면 좋아요 취소
    }

    /**
     * 특정 사용자가 특정 회고에 좋아요를 눌렀는지 확인
     */
    public boolean isLiked(String userId, Long retrospectId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getLikeKey(userId, retrospectId)));
    }

    /**
     * 특정 회고의 좋아요 개수 조회
     */
    public int getLikeCount(Long retrospectId) {
        Integer likeCount = redisCountTemplate.opsForValue().get(getLikeCountKey(retrospectId));
        return likeCount != null ? likeCount : 0;
    }
}
