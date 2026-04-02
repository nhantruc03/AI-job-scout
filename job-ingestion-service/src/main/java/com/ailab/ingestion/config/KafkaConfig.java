package com.ailab.ingestion.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaTopicProperties kafkaTopicProperties;

    @Bean
    public NewTopics createTopics() {
        var newTopics = kafkaTopicProperties.getTopics().stream()
                .map(topic -> TopicBuilder.name(topic.getName())
                        .partitions(topic.getPartitions())
                        .replicas(topic.getReplicas())
                        .build())
                .toArray(NewTopic[]::new);

        return new NewTopics(newTopics);
    }
}
