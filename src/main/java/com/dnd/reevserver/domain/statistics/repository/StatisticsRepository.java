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

        // Update if exists, else insert
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .updateExpression("SET cnt = if_not_exists(cnt, :zero) + :incr, month = :month")
                .expressionAttributeValues(Map.of(
                        ":zero", AttributeValue.fromN("0"),
                        ":incr", AttributeValue.fromN("1"),
                        ":month", AttributeValue.fromS(month)
                ))
                .build();

        dynamoDbClient.updateItem(request);
    }

    public List<Map<String, AttributeValue>> getUserRetrospectStats(String userId, LocalDate month) {
        String monthStr = month.toString();
        String startDate = monthStr + "-01";
        String endDate = monthStr + "-31";

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression("userId = :userId AND #date BETWEEN :startDate AND :endDate")
                .expressionAttributeNames(Map.of("#date", "date"))
                .expressionAttributeValues(Map.of(
                        ":userId", AttributeValue.fromS(userId),
                        ":startDate", AttributeValue.fromS(startDate),
                        ":endDate", AttributeValue.fromS(endDate)
                ))
                .build();

        QueryResponse response = dynamoDbClient.query(queryRequest);
        return response.items();
    }
}