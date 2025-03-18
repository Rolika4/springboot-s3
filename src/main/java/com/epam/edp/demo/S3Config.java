package com.epam.edp.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    @Value("${region}")
    private String region;

    @Bean
    S3Client s3Client() {
        WebIdentityTokenFileCredentialsProvider credentialsProvider = WebIdentityTokenFileCredentialsProvider.create();

        logger.info("Using AWS credentials provider: {}", credentialsProvider.getClass().getSimpleName());
        logger.info("Web Identity Token will be used for authentication.");

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
