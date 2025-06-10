package com.dnd.reevserver.global.config.sqs;

import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;

import java.time.Duration;

@Configuration
@Import(SqsBootstrapConfiguration.class)
public class SQSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String AWS_ACCESS_KEY;

    @Value("${cloud.aws.credentials.secret-key}")
    private String AWS_SECRET_KEY;

    @Value("${cloud.aws.region.static}")
    private String AWS_REGION;

    private AwsCredentials credentials() {
        return new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return AWS_ACCESS_KEY;
            }

            @Override
            public String secretAccessKey() {
                return AWS_SECRET_KEY;
            }
        };
    }

    @Bean(name = "likeSqsClient")
    public SqsAsyncClient likeSqsClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(this::credentials)
                .httpClientBuilder(
                        NettyNioAsyncHttpClient.builder()
                                .maxConcurrency(100)
                                .connectionAcquisitionTimeout(Duration.ofSeconds(5))
                )
                .build();
    }

    @Bean(name = "alertSqsClient")
    public SqsAsyncClient alertSqsClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(this::credentials)
                .httpClientBuilder(
                        NettyNioAsyncHttpClient.builder()
                                .maxConcurrency(100)
                                .connectionAcquisitionTimeout(Duration.ofSeconds(5))
                )
                .build();
    }

    @Bean(name = "likeSqsTemplate")
    public SqsTemplate likeSqsTemplate(@Qualifier("likeSqsClient") SqsAsyncClient likeClient) {
        return SqsTemplate.newTemplate(likeClient);
    }

    @Bean(name = "alertSqsTemplate")
    public SqsTemplate alertSqsTemplate(@Qualifier("alertSqsClient") SqsAsyncClient alertClient) {
        return SqsTemplate.newTemplate(alertClient);
    }

    @Bean(name = "alertSqsListenerContainerFactory")
    public SqsMessageListenerContainerFactory<Object> alertSqsListenerContainerFactory(
            @Qualifier("alertSqsClient") SqsAsyncClient alertClient) {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(alertClient)
                .build();
    }
}