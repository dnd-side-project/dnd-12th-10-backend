package com.dnd.reevserver.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final DynamoDbClient dynamoDbClient;

    @Value("${cloud.aws.dynamodb.name.like.islike}")
    private String USER_LIKE_TABLE;

    @Value("${cloud.aws.dynamodb.name.like.count}")
    private String COUNT_TABLE;

    public boolean isLiked(String userId, Long retrospectId) {
        String likeKey = retrospectId + ":" + userId;

        GetItemRequest request = GetItemRequest.builder()
                .tableName(USER_LIKE_TABLE)
                .key(Map.of("likeKey", AttributeValue.builder().s(likeKey).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        return response.hasItem();
    }

    public int getLikeCount(Long retrospectId) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(COUNT_TABLE)
                .key(Map.of("retrospectId", AttributeValue.builder().s(String.valueOf(retrospectId)).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem() || !response.item().containsKey("count")) {
            return 0;
        }

        return Integer.parseInt(response.item().get("count").n());
    }
}
