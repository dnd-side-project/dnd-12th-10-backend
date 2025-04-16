package com.dnd.reevserver.domain.statistics.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final DynamoDbClient dynamoDbClient;

    @Value("${cloud.aws.dynamodb.name.statistics.retrospect-count}")
    private String TABLE_NAME;

    public void incrementRetrospectCount(String userId, LocalDate date) {
        String dateStr = date.toString();
        String month = dateStr.substring(0, 7);

        Map<String, AttributeValue> key = Map.of(
                "userId", AttributeValue.fromS(userId),
                "date", AttributeValue.fromS(dateStr)
        );

        // 있으면 업데이트, 없으면 추가
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("SET cnt = if_not_exists(cnt, :zero) + :incr, #month = :month")
                .expressionAttributeNames(Map.of(
                        "#month", "month"
                ))
                .expressionAttributeValues(Map.of(
                        ":zero", AttributeValue.fromN("0"),
                        ":incr", AttributeValue.fromN("1"),
                        ":month", AttributeValue.fromS(month)
                ))
                .build();

        dynamoDbClient.updateItem(request);
    }

    public List<Map<String, AttributeValue>> getUserRetrospectStats(String userId, LocalDate startDate, LocalDate endDate) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("userId = :userId AND #date BETWEEN :start AND :end")
                .expressionAttributeNames(Map.of("#date", "date"))
                .expressionAttributeValues(Map.of(
                        ":userId", AttributeValue.fromS(userId),
                        ":start", AttributeValue.fromS(startDate.toString()),
                        ":end", AttributeValue.fromS(endDate.toString())
                ))
                .build();

        return dynamoDbClient.query(queryRequest).items();
    }
}