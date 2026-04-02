package com.ailab.ingestion.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaTopicProperties {
    private List<TopicConfig> topics;

    @Data
    public static class TopicConfig {
        private String name;
        private int partitions = 1;
        private short replicas = 1;
    }
}
