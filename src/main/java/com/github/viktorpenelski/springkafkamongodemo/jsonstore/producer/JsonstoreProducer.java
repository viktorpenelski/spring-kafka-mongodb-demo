package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.UUID;

@Service
public class JsonstoreProducer {

    private static final Logger logger = LoggerFactory.getLogger(JsonstoreProducer.class);

    //TODO(vic) Consider using UUID key instead of String.
    // In kafka 2.1.0 UUID serde exists. The project currently inherits 2.0.1 from the spring BOM.
    private KafkaTemplate<String, JsonstoreRecord> kafkaTemplate;
    private String topic;
    private MessageUUIDGenerator uuidGenerator;

    public JsonstoreProducer(KafkaTemplate<String, JsonstoreRecord> kafkaTemplate,
                             @Value("${jsonstore.kafka.topic}") String kafkaTopic,
                             MessageUUIDGenerator uuidGenerator) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaTopic;
        this.uuidGenerator = uuidGenerator;
    }

    public UUID sendMessage(Map<String, Object> message) {

        UUID key = uuidGenerator.generateUUID();
        JsonstoreRecord payload = new JsonstoreRecord(key, message);
        logger.info("Attempting to send to topic {}, message: {}", topic, payload);

        kafkaTemplate.send(topic, key.toString(), payload)
                .addCallback(new ListenableFutureCallback<>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        logger.error("Failed to send message with id: {} to topic: {}!", key, topic);
                    }

                    @Override
                    public void onSuccess(SendResult<String, JsonstoreRecord> result) {
                        logger.info("Successfully sent message with id: {} to topic: {}", key, topic);
                    }
                });

        return key;
    }

}