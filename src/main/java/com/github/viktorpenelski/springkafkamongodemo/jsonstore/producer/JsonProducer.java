package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class JsonProducer {

    private static final Logger logger = LoggerFactory.getLogger(JsonProducer.class);

    private KafkaTemplate<String, JsonstoreRecord> kafkaTemplate;
    private String topic;
    private MessageUUIDGenerator uuidGenerator;

    public JsonProducer(KafkaTemplate<String, JsonstoreRecord> kafkaTemplate,
                        @Value("${jsonstore.kafka.topic}") String kafkaTopic,
                        MessageUUIDGenerator uuidGenerator) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaTopic;
        this.uuidGenerator = uuidGenerator;
    }

    public UUID sendMessage(Map<String, Object> message) {

        UUID key = uuidGenerator.generateUUID();
        logger.info("Producing to topic {}, key: {}, message: {}", topic, key.toString(), message);
        this.kafkaTemplate.send(topic, key.toString(), new JsonstoreRecord(key, message));
        return key;
    }

}