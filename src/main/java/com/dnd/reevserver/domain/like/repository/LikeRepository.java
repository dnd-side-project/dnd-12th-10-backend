package com.dnd.reevserver.domain.like.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {
    @Qualifier("redisBooleanTemplate") // 특정 RedisTemplate을 지정
    private final RedisTemplate<String, Boolean> redisTemplate;

    public LikeRepository(RedisTemplate<String, Boolean> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 유저가 해당 게시글에 좋아요를 눌렀는지가 키가 됨. 값은 좋아요 여부
    private String getLikeKey(String userId, Long retrospectId) {
        return userId + ":" + retrospectId;
    }

    // 좋아요
    public void addLike(String userId, Long retrospectId) {
        redisTemplate.opsForValue().set(getLikeKey(userId, retrospectId), true); // 좋아요 상태 저장
    }

    // 좋아요 취소
    public void removeLike(String userId, Long retrospectId) {
        redisTemplate.delete(getLikeKey(userId, retrospectId)); // 좋아요 상태 삭제
    }

    // 특정 유저가 특정 게시글에 좋아요를 눌렀는지 확인
    public boolean isLiked(String userId, Long retrospectId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getLikeKey(userId, retrospectId)));
    }
}
