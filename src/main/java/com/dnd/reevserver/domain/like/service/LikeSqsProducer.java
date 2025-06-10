package com.dnd.reevserver.domain.like.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class LikeSqsProducer {

    private final SqsAsyncClient sqsAsyncClient;

    public LikeSqsProducer(@Qualifier("likeSqsClient") SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Value("${cloud.aws.sqs.queue-like-name}")
    private String queueUrl;

    public void sendToggleLikeEvent(Long retrospectId, String userId) {
        String body = """
            {
              "retrospectId": "%s",
              "userId": "%s"
            }
            """.formatted(retrospectId, userId);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(body)
                .build();

        sqsAsyncClient.sendMessage(request);
    }
}
