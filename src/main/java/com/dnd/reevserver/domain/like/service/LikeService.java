package com.dnd.reevserver.domain.like.service;

import com.dnd.reevserver.domain.alert.service.AlertService;
import com.dnd.reevserver.domain.like.dto.request.LikeRequestDto;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final RetrospectService retrospectService;
    private final MemberService memberService;
    private final AlertService alertService;


    // 좋아요 토글 (추가 또는 취소)
    public boolean toggleLike(String userId, LikeRequestDto dto) {
        Long retrospectId = dto.retrospectId();
        Member member = memberService.findById(userId);
        Retrospect retrospect = retrospectService.findById(retrospectId);
        boolean result = likeRepository.toggleLike(userId, retrospectId);
        if(result) alertService.sendMessage(member.getName() + "님이 " + retrospect.getTitle() + "에 좋아요를 눌렀습니다. [" + retrospectId + "]");
        return result;
    }


    // 특정 사용자가 특정 회고에 좋아요를 눌렀는지 확인
    public boolean isLiked(String userId, Long retrospectId) {
        return likeRepository.isLiked(userId, retrospectId);
    }
}
