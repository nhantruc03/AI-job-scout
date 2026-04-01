package com.ailab.ingestion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(String key, InputStream inputStream, long contentLength) {
        log.info("Uploading file to S3: {}/{}", bucketName, key);
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
        
        return key;
    }
}
