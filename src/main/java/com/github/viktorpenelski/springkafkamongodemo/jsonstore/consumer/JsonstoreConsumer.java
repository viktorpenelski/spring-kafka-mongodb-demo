package com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer.web.JsonstoreRepository;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonstoreConsumer {

    private final Logger logger = LoggerFactory.getLogger(JsonstoreConsumer.class);

    private JsonstoreRepository repository;

    public JsonstoreConsumer(JsonstoreRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${jsonstore.kafka.topic}")
    public void consume(JsonstoreRecord message) {

        try {
            repository.save(message);
            logger.info("Successfully persisted: {}", message);
        } catch (Exception e) {
            logger.error("Failed to persist message with id: {}", message.getId());
        }

    }
}