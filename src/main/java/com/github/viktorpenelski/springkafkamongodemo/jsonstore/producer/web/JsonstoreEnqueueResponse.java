package com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.web;

import java.util.UUID;

public class JsonstoreEnqueueResponse {
    private String message;
    private UUID id;
    private String href;

    public static JsonstoreEnqueueResponse from(UUID id) {
        JsonstoreEnqueueResponse response = new JsonstoreEnqueueResponse();
        response.message = "Successfully enqueued the payload. Resource will be available shortly.";
        response.id = id;
        response.href = "/jsonstore/" + id;

        return response;
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }

    public String getHref() {
        return href;
    }
}
