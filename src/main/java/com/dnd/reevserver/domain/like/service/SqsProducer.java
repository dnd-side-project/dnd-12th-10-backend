package com.dnd.reevserver.domain.like.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsProducer {

    private final AmazonSQS amazonSQS;

    @Value("${cloud.aws.sqs.queue-like-name}")
    String queueUrl;

    public void sendToggleLikeEvent(Long retrospectId, String userId) {
        String body = """
            {
              "retrospectId": "%s",
              "userId": "%s"
            }
            """.formatted(retrospectId, userId);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(body);

        amazonSQS.sendMessage(request);
    }
}
