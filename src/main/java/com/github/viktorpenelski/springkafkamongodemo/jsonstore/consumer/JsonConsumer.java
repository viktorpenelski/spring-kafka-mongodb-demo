package com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class JsonConsumer {

    private final Logger logger = LoggerFactory.getLogger(JsonConsumer.class);

    //TODO(vic) typization
    @KafkaListener(topics = "${jsonstore.topic:jsonstore}")
    public void consume(
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY)
                    String key,
            String message) {

        logger.info("Consumed key: {}, body: {}", key, message);
    }
}