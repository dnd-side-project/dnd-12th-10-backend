package com.dnd.reevserver.domain.like.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
public class LikeSqsProducer {

    private final SqsAsyncClient sqsAsyncClient;

    private static final int MAX_CONCURRENT_SENDS = 20;
    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_SENDS);

    @Value("${cloud.aws.sqs.queue-like-name}")
    private String queueUrl;

    public LikeSqsProducer(@Qualifier("likeSqsClient") SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    public void sendToggleLikeEvent(Long retrospectId, String userId) {
        try {
            boolean acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS);

            if (!acquired) {
                throw new RuntimeException("SQS 좋아요 전송 제한 초과: 메시지를 건너뜁니다.");
            }

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

            sqsAsyncClient.sendMessage(request)
                    .whenComplete((resp, ex) -> {
                        semaphore.release();
                        if (ex != null) {
                            System.err.println("좋아요 SQS 전송 실패: " + ex.getMessage());
                        }
                    });

        } catch (Exception e) {
            semaphore.release();
            throw new RuntimeException("SQS 좋아요 메시지 전송 중 오류 발생", e);
        }
    }
}
