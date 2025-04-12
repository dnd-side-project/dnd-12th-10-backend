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

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final RetrospectService retrospectService;
    private final MemberService memberService;
    private final AlertService alertService;
    private final SqsProducer sqsProducer;
    private final LikeRepository likeRepository;

    public void toggleLike(String userId, LikeRequestDto dto) {
        Long retrospectId = dto.retrospectId();
        sqsProducer.sendToggleLikeEvent(retrospectId, userId);

        Member member = memberService.findById(userId);
        Retrospect retrospect = retrospectService.findById(retrospectId);
        alertService.sendMessage(member.getName() + "님이 " + retrospect.getTitle() + "에 좋아요를 눌렀습니다. [" + retrospectId + "]");
    }

    public boolean isLiked(String userId, Long retrospectId) {
        return likeRepository.isLiked(userId, retrospectId);
    }
}
