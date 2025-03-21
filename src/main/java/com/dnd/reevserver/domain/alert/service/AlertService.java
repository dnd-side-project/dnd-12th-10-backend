package com.dnd.reevserver.domain.alert.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Flux;

@Service
public class AlertService {
    private final QueueMessagingTemplate queueMessagingTemplate;

    // SSE를 위한 Flux Sink 생성 (SSE 클라이언트들에게 메시지 푸시)
    private final Sinks.Many<String> messageSink = Sinks.many().multicast().onBackpressureBuffer();

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    public AlertService(AmazonSQSAsync amazonSQS){
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQS);
    }

    public void sendMessage(String message){
        Message<String> newMessage = MessageBuilder.withPayload(message).build();
        queueMessagingTemplate.send(queueName, newMessage);
    }

    public Flux<String> getMessageStream() {
        return messageSink.asFlux();
    }
}
