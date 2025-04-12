package com.dnd.reevserver.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Duration;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "refresh_token_table"; // 테이블 이름
    private static final String PK = "userId";

    public void save(String userId, String token, Duration expiration) {
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(Map.of(
                        PK, AttributeValue.fromS(userId),
                        "refreshToken", AttributeValue.fromS(token),
                        "ttl", AttributeValue.fromN(String.valueOf((System.currentTimeMillis() / 1000) + expiration.getSeconds()))
                ))
                .build());
    }

    public String findByKey(String userId) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(PK, AttributeValue.fromS(userId)))
                .build());

        return response.hasItem() ? response.item().get("refreshToken").s() : null;
    }

    public void delete(String userId) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(PK, AttributeValue.fromS(userId)))
                .build());
    }
}
