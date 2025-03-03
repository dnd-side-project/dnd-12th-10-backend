package com.dnd.reevserver.domain.statistics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import java.time.LocalDate;

@Service
public class LambdaService {
    private final LambdaClient lambdaClient;

    @Value("${lambda.function.write}")
    private String writeFunctionName;

    public LambdaService(@Value("${cloud.aws.region.static}") String AWS_REGION,
                         @Value("${cloud.aws.credentials.access-key}") String ACCESS_KEY,
                         @Value("${cloud.aws.credentials.secret-key}") String SECRET_KEY) {
        this.lambdaClient = LambdaClient.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return ACCESS_KEY;
                    }

                    @Override
                    public String secretAccessKey() {
                        return SECRET_KEY;
                    }
                })
                .build();
    }

    public void writeStatistics(String userId){
        String payload = String.format("{\"userId\": \"%s\", \"date\": \"%s\"}", userId, LocalDate.now());
        triggerLambda(writeFunctionName, payload);
    }

    private void triggerLambda(String functionName, String payload) {
        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload)) // JSON 데이터 전달
                .invocationType(InvocationType.EVENT) // 비동기 실행 설정
                .build();

        lambdaClient.invoke(request);
    }
}
