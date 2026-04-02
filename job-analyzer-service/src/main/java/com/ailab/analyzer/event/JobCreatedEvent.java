package com.ailab.analyzer.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCreatedEvent {
    private String jobId;
    private String s3Key;
    private String status;
}
