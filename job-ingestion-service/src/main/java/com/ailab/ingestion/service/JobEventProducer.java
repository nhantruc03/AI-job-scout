package com.ailab.ingestion.service;

import com.ailab.ingestion.model.JobCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishJobCreated(JobCreatedEvent event) {
        log.info("### Producing event ### : {}", event);
        // Gửi trực tiếp bằng tên topic (đã được config động)
        kafkaTemplate.send("job-events-topic", event.getJobId(), event);
    }
}
