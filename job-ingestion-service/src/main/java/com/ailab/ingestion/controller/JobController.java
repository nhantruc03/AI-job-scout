package com.ailab.ingestion.controller;

import com.ailab.ingestion.model.JobCreatedEvent;
import com.ailab.ingestion.model.JobMetadata;
import com.ailab.ingestion.service.DynamoDbService;
import com.ailab.ingestion.service.JobEventProducer;
import com.ailab.ingestion.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final StorageService storageService;
    private final DynamoDbService dynamoDbService;
    private final JobEventProducer jobEventProducer;

    @GetMapping
    public ResponseEntity<List<JobMetadata>> getAllJobs() {
        log.info("Fetching all job metadata");
        return ResponseEntity.ok(dynamoDbService.getAllJobMetadata());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadJob(@RequestParam("file") MultipartFile file) {
        String jobId = UUID.randomUUID().toString();
        log.info("Received request to upload JD. Assigning ID: {}", jobId);

        try {
            // 1. Upload file lên S3
            String s3Key = "jds/" + jobId + "-" + file.getOriginalFilename();
            storageService.uploadFile(s3Key, file.getInputStream(), file.getSize());

            // 2. Lưu Metadata vào DynamoDB
            JobMetadata metadata = JobMetadata.builder()
                    .jobId(jobId)
                    .originalFileName(file.getOriginalFilename())
                    .s3Key(s3Key)
                    .status("PENDING")
                    .createdAt(Instant.now().toString())
                    .build();

            dynamoDbService.saveJobMetadata(metadata);

            // 3. Publish Event vào Kafka
            jobEventProducer.publishJobCreated(JobCreatedEvent.builder()
                    .jobId(jobId)
                    .s3Key(s3Key)
                    .status("PENDING")
                    .build());

            return ResponseEntity.ok("Job uploaded successfully. ID: " + jobId);

        } catch (IOException e) {
            log.error("Error processing job upload", e);
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
