package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JsonProducer {

    private static final Logger logger = LoggerFactory.getLogger(JsonProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;
    private String topic;
    private MessageUUIDGenerator uuidGenerator;

    public JsonProducer(KafkaTemplate<String, String> kafkaTemplate,
                        @Value("${jsonstore.topic:jsonstore}") String kafkaTopic,
                        MessageUUIDGenerator uuidGenerator) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaTopic;
        this.uuidGenerator = uuidGenerator;
    }

    public UUID sendMessage(String message) {

        UUID key = uuidGenerator.generateUUID();
        logger.info("Producing to topic {}, key: {}, message: {}", topic, key.toString(), message);
        this.kafkaTemplate.send(topic, key.toString(), message);
        return key;
    }

}