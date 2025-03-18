package com.epam.edp.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class S3Service {
    private final S3Client s3Client;
    private final String bucket;
    private final String fileName;
    private final StsClient stsClient;

    public S3Service (S3Client s3Client,
                      @Value("${bucket.name}") String bucket,
                      @Value("${file.name}") String fileName, StsClient stsClient) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.fileName = fileName;
        this.stsClient = stsClient;
    }

    public String readFile() {
        String content = readFile(bucket, fileName);
        log.info("Content='{}' form bucket='{}', key='{}'", content, bucket, fileName);
        return content;
    }

    private String readFile(String bucket, String fileName) {
        // additional logging
        GetCallerIdentityResponse identity = stsClient.getCallerIdentity(
                GetCallerIdentityRequest.builder().build()
        );
        log.info("Going to read file {} from {} using AWS Role: ARN={}, Account={}, UserId={}",
                fileName,
                bucket,
                identity.arn(),
                identity.account(),
                identity.userId());

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);

        try {
            return new String(responseInputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error reading bucket={}, fileName={}", bucket, fileName, e);
            throw new RuntimeException(e);
        }

    }
}
 