package com.dnd.reevserver.domain.like.service;

import com.dnd.reevserver.domain.alert.service.AlertService;
import com.dnd.reevserver.domain.alert.service.AlertSqsProducer;
import com.dnd.reevserver.domain.like.dto.request.LikeRequestDto;
import com.dnd.reevserver.domain.like.repository.LikeRepository;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final RetrospectService retrospectService;
    private final MemberService memberService;
    private final AlertService alertService;
    private final LikeSqsProducer likeSqsProducer;
    private final LikeRepository likeRepository;

    public boolean toggleLike(String userId, LikeRequestDto dto) {
        Long retrospectId = dto.retrospectId();
        likeSqsProducer.sendToggleLikeEvent(retrospectId, userId);

        Member member = memberService.findById(userId);
        Retrospect retrospect = retrospectService.findById(retrospectId);
        boolean isUnLiked = isLiked(userId, retrospectId); // DB에 추가해도 없다고 뜸 -> 반영이 안된 상태 -> 즉 거꾸로 뜰 거임
        if(!isUnLiked){
            alertService.sendMessage(
                    UUID.randomUUID().toString(),
                    retrospect.getMember().getUserId(),
                    member.getName() + "님이 " + retrospect.getTitle() + "에 좋아요를 눌렀습니다. [" + retrospectId + "]",
                    LocalDateTime.now(),
                    retrospectId);
        }
        return isUnLiked;
    }

    public boolean isLiked(String userId, Long retrospectId) {
        return likeRepository.isLiked(userId, retrospectId);
    }
}
