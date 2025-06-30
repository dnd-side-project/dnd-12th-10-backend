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

@Repository
@RequiredArgsConstructor
public class TeamLinkRepository {
    private final DynamoDbClient dynamoDbClient;

    @Value("${cloud.aws.dynamodb.name.team.link}")
    private String TABLE_NAME;

    private static final String PK = "uuid";

    public void save(String uuid, Duration expiration) {
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(Map.of(
                        PK, AttributeValue.fromS(uuid),
                        "ttl", AttributeValue.fromN(String.valueOf((System.currentTimeMillis() / 1000) + expiration.getSeconds()))
                ))
                .build());
    }

    public boolean existsUuid(String uuid) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(PK, AttributeValue.fromS(uuid)))
                .build());

        return response.hasItem();
    }
}
