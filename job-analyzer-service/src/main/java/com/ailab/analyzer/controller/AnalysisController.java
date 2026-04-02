package com.ailab.analyzer.controller;

import com.ailab.analyzer.model.JobMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final DynamoDbEnhancedClient enhancedClient;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @GetMapping("/status/{jobId}")
    public JobMetadata getAnalysisStatus(@PathVariable String jobId) {
        DynamoDbTable<JobMetadata> table = enhancedClient.table(tableName, TableSchema.fromBean(JobMetadata.class));
        return table.getItem(r -> r.key(k -> k.partitionValue(jobId)));
    }
}
