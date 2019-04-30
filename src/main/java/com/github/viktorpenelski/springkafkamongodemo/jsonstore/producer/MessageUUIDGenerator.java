package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer;

import java.util.UUID;

/**
 * Generator for UUIDs.
 * <p>
 * Utilizes the core {@link UUID} class for representation.
 * Implementations are responsible for the generation and no guarantee is provided for the type of the generated UUID.
 */
public interface MessageUUIDGenerator {

    /**
     * Generate a valid {@link UUID}.
     * Universal uniqueness is expected.
     * The implementation decides on the type to be generated.
     */
    UUID generateUUID();
}
