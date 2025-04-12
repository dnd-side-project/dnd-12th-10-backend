package com.dnd.reevserver.domain.like.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsProducer {

    private final AmazonSQS amazonSQS;

    public void sendToggleLikeEvent(Long retrospectId, String userId) {
        String body = """
            {
              "retrospectId": "%s",
              "userId": "%s"
            }
            """.formatted(retrospectId, userId);

        String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/{your-account-id}/{your-queue-name}";
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(body);

        amazonSQS.sendMessage(request);
    }
}
