package com.dnd.reevserver.domain.statistics.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

@Service
public class LambdaService {
    private final LambdaClient lambdaClient;

    public LambdaService() {
        this.lambdaClient = LambdaClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create()) // IAM Credentials 자동 감지
                .build();
    }

    public void triggerLambda(String functionName, String payload) {
        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload)) // JSON 데이터 전달
                .invocationType(InvocationType.EVENT) // 비동기 실행 설정
                .build();

        lambdaClient.invoke(request);
    }
}
