package com.ailab.analyzer.consumer;

import com.ailab.analyzer.event.JobCreatedEvent;
import com.ailab.analyzer.model.JobMetadata;
import com.ailab.analyzer.service.AnalyzerAgent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobEventConsumer {

    private final S3Client s3Client;
    private final DynamoDbEnhancedClient enhancedClient;
    private final AnalyzerAgent analyzerAgent;

    private ExecutorService executorService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @PostConstruct
    void init() {
        executorService = new ThreadPoolExecutor(
                50,
                100,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @KafkaListener(topics = "job-events-topic", groupId = "analyzer-group")
    public void consumeJobCreatedEvent(JobCreatedEvent event) {
        log.info("Received JobCreatedEvent: {}", event);

        CompletableFuture.runAsync(() -> {
            // 1. Fetch JD content from S3 (Performed in background)
            log.info("Fetching JD from S3: bucket={}, key={}", bucketName, event.getS3Key());
            byte[] contentBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(event.getS3Key())
                    .build()).asByteArray();
            
            String jdContent = extractTextContent(event.getS3Key(), contentBytes);

            // 2. Call AI Analyzer (LangChain4j)
            log.info("Analyzing JD with AI...");
            String aiResponse = analyzerAgent.analyzeJd(jdContent);
            log.info("AI Response: {}", aiResponse);

            // 3. Parse Analysis Result
            String summary = parseLine(aiResponse, "Summary:");
            String score = parseLine(aiResponse, "Score:");
            String suggestion = parseLine(aiResponse, "Suggestion:");

            // 4. Update DynamoDB
            updateJobMetadata(event.getJobId(), summary, score, suggestion);
            log.info("Successfully analyzed and updated job: {}", event.getJobId());
        }, executorService).exceptionally(ex -> {
            log.error("Error processing JobCreatedEvent: {}", event.getJobId(), ex);
            return null;
        });

        log.info("Accepted message for processing: {}", event.getJobId());
    }

    private void updateJobMetadata(String jobId, String summary, String score, String suggestion) {
        DynamoDbTable<JobMetadata> table = enhancedClient.table(tableName, TableSchema.fromBean(JobMetadata.class));

        JobMetadata metadata = table.getItem(r -> r.key(k -> k.partitionValue(jobId)));
        if (metadata != null) {
            metadata.setAiScore(score);
            metadata.setAiSummary(summary);
            metadata.setStatus("ANALYZED");
            metadata.setAiSuggestion(suggestion);
            table.updateItem(metadata);
        } else {
            log.warn("JobMetadata not found for jobId: {}", jobId);
        }
    }

    private String parseLine(String text, String prefix) {
        for (String line : text.split("\n")) {
            if (line.trim().startsWith(prefix)) {
                return line.replace(prefix, "").trim();
            }
        }
        return "N/A";
    }

    private String extractTextContent(String key, byte[] content) {
        if (key.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(new ByteArrayInputStream(content))) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            } catch (IOException e) {
                log.error("Failed to parse PDF content for key: {}", key, e);
                return "Error parsing PDF content";
            }
        }
        return new String(content, StandardCharsets.UTF_8);
    }
}
