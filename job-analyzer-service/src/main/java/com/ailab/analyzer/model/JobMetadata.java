package com.ailab.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class JobMetadata {
    private String jobId;
    private String originalFileName;
    private String s3Key;
    private String status;
    private String aiScore;
    private String createdAt;
    private String aiSummary;
    private String aiSuggestion;

    @DynamoDbPartitionKey
    public String getJobId() {
        return jobId;
    }
}
