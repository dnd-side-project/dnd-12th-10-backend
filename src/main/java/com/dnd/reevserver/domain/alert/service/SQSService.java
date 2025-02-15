package com.dnd.reevserver.domain.alert.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SQSService {
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    public SQSService(AmazonSQSAsync amazonSQS){
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQS);
    }

    public void sendMessage(String message){
        Message<String> newMessage = MessageBuilder.withPayload(message).build();
        queueMessagingTemplate.send(queueName, newMessage);
    }
}
