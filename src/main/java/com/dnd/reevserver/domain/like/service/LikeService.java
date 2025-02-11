package com.dnd.reevserver.domain.like.service;

import com.dnd.reevserver.domain.like.dto.request.LikeRequestDto;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final RetrospectService retrospectService;

    @Transactional
    public boolean toggleLike(String userId, LikeRequestDto dto){
        Long retrospectId = dto.retrospectId();
        if(likeRepository.isLiked(userId, retrospectId)){
            likeRepository.removeLike(userId, retrospectId);
            retrospectService.updateLikeCnt(retrospectId, true);
            return false;
        } else {
            likeRepository.addLike(userId, retrospectId);
            retrospectService.updateLikeCnt(retrospectId, false);
            return true;
        }
    }

    // 특정 유저가 특정 게시글에 좋아요를 눌렀는지 확인
    public boolean isLiked(String userId, Long retrospectId) {
        return likeRepository.isLiked(userId, retrospectId);
    }
}
