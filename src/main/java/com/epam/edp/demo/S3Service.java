package com.epam.edp.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final String bucketName;
    private final String fileName;
    private final S3Client s3Client;

    public S3Service(
            @Value("${bucket.name}") String bucketName,
            @Value("${file.name}") String fileName,
            @Autowired S3Client s3Client) {
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.s3Client = s3Client;
    }

    public String getData() {
        ResponseInputStream<GetObjectResponse> s3ObjectResponse = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build());

        return new BufferedReader(new InputStreamReader(s3ObjectResponse, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

}
