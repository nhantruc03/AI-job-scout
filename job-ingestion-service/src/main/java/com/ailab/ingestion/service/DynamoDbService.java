package com.ailab.ingestion.service;

import com.ailab.ingestion.model.JobMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DynamoDbService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName;
    private DynamoDbTable<JobMetadata> jobTable;

    public DynamoDbService(DynamoDbEnhancedClient enhancedClient, 
                           @Value("${aws.dynamodb.table-name}") String tableName) {
        this.enhancedClient = enhancedClient;
        this.tableName = tableName;
    }

    @PostConstruct
    public void init() {
        // "Móc" Object JobMetadata vào Table trên AWS
        this.jobTable = enhancedClient.table(tableName, TableSchema.fromBean(JobMetadata.class));
    }

    public void saveJobMetadata(JobMetadata metadata) {
        log.info("Saving metadata to DynamoDB: {}", metadata.getJobId());
        jobTable.putItem(metadata);
    }

    public List<JobMetadata> getAllJobMetadata() {
        log.info("Scanning for all job metadata in DynamoDB");
        return jobTable.scan().items().stream().collect(Collectors.toList());
    }
}
