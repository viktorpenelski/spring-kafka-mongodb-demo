package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomMessageUUIDGenerator implements MessageUUIDGenerator {

    /**
     * Generate a type 4 pseudo-random UUID, utilizing {@link UUID#randomUUID()}
     *
     * @return the pseudo-randomly generated UUID.
     */
    @Override
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
