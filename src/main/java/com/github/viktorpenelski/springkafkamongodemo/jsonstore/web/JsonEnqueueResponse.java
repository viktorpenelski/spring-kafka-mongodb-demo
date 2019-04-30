package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public class JsonEnqueueResponse {
    private UUID id;
    private JsonNode payload;

    public JsonEnqueueResponse(UUID id, JsonNode payload) {
        this.id = id;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}
