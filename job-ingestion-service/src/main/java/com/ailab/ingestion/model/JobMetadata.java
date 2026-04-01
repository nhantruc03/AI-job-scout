package com.ailab.ingestion.model;

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
@DynamoDbBean // Giống như @Entity trong Hibernate nhưng dành cho DynamoDB
public class JobMetadata {
    private String jobId;
    private String originalFileName;
    private String s3Key;
    private String status; // PENDING, ANALYZED
    private String aiScore;
    private String createdAt;

    @DynamoDbPartitionKey // Định nghĩa Khóa chính (Partition Key)
    public String getJobId() {
        return jobId;
    }
}
