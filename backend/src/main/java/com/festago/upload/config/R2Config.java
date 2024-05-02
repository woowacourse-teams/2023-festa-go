package com.festago.upload.config;

import com.festago.upload.infrastructure.R2StorageClient;
import java.net.URI;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class R2Config {

    private final String accessKey;
    private final String secretKey;
    private final String endpoint;

    public R2Config(
        @Value("${festago.r2.access-key}") String accessKey,
        @Value("${festago.r2.secret-key}") String secretKey,
        @Value("${festago.r2.endpoint}") String endpoint
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
    }

    @Bean
    public R2StorageClient r2UploadClient(
        @Value("${festago.r2.bucket}") String bucket,
        @Value("${festago.r2.url}") String uri,
        Clock clock
    ) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        S3Client s3Client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
            .endpointOverride(URI.create(endpoint))
            .region(Region.of("auto"))
            .build();
        return new R2StorageClient(s3Client, bucket, URI.create(uri), clock);
    }
}
