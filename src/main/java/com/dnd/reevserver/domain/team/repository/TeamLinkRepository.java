package com.dnd.reevserver.domain.team.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamLinkRepository {
    private final DynamoDbClient dynamoDbClient;

    @Value("${cloud.aws.dynamodb.name.team.link}")
    private String TABLE_NAME;

    private static final String PK = "uuid";

    public void save(String uuid, Long groupId, Duration expiration) {
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(Map.of(
                        PK, AttributeValue.fromS(uuid),
                        "groupId", AttributeValue.fromS(String.valueOf(groupId)),
                        "ttl", AttributeValue.fromN(String.valueOf((System.currentTimeMillis() / 1000) + expiration.getSeconds()))
                ))
                .build());
    }

    public Optional<Long> findGroupIdByUuid(String uuid) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(PK, AttributeValue.fromS(uuid)))
                .build());

        if (!response.hasItem()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = response.item();
        AttributeValue groupIdAttr = item.get("groupId");

        if (groupIdAttr == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(groupIdAttr.s()));
        } catch (NumberFormatException e) {
            return Optional.empty(); // 잘못 저장된 경우도 방어
        }
    }

}
