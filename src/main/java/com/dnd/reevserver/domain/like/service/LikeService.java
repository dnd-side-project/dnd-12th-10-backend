package com.dnd.reevserver.domain.like.service;

import com.dnd.reevserver.domain.alert.service.AlertService;
import com.dnd.reevserver.domain.like.dto.request.LikeRequestDto;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.MemberService;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final RetrospectService retrospectService;
    private final MemberService memberService;
    private final AlertService alertService;
    private final SqsProducer sqsProducer;

    private final DynamoDbClient dynamoDbClient;

    private static final String LIKE_TABLE = "retrospect_user_isLike_table";

    public void toggleLike(String userId, LikeRequestDto dto) {
        Long retrospectId = dto.retrospectId();
        sqsProducer.sendToggleLikeEvent(retrospectId, userId);

        Member member = memberService.findById(userId);
        Retrospect retrospect = retrospectService.findById(retrospectId);
        alertService.sendMessage(member.getName() + "님이 " + retrospect.getTitle() + "에 좋아요를 눌렀습니다. [" + retrospectId + "]");
    }

    // 특정 사용자가 특정 회고에 좋아요를 눌렀는지 확인
    public boolean isLiked(String userId, Long retrospectId) {
        String likeKey = retrospectId + ":" + userId;

        GetItemRequest request = GetItemRequest.builder()
                .tableName(LIKE_TABLE)
                .key(Map.of("likeKey", AttributeValue.builder().s(likeKey).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        return response.hasItem(); // 아이템 존재 여부로 좋아요 상태 판단
    }

    public int getLikeCount(Long retrospectId) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName("retrospect_like_count_table")
                .key(Map.of("retrospectId", AttributeValue.builder().s(String.valueOf(retrospectId)).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem() || !response.item().containsKey("count")) {
            return 0; // 좋아요 수 없으면 기본값 0
        }

        return Integer.parseInt(response.item().get("count").n());
    }
}
