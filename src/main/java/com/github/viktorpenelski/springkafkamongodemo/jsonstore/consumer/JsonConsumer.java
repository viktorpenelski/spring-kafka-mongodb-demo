package com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.JsonstoreRepository;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonConsumer {

    private final Logger logger = LoggerFactory.getLogger(JsonConsumer.class);

    private JsonstoreRepository repository;

    public JsonConsumer(JsonstoreRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${jsonstore.kafka.topic}")
    public void consume(JsonstoreRecord message) {

        repository.save(message);
        logger.info("Successfully persisted: {}", message);

    }
}