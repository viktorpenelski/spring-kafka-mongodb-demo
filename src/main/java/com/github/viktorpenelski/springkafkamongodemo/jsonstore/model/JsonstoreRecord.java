package com.github.viktorpenelski.springkafkamongodemo.jsonstore.model;

import org.springframework.data.annotation.Id;

import java.util.Map;
import java.util.UUID;

public class JsonstoreRecord {

    @Id
    private UUID id;

    private Map<String, Object> payload;

    public JsonstoreRecord() {

    }

    public JsonstoreRecord(UUID id, Map<String, Object> payload) {
        this.id = id;
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "JsonstoreRecord{" +
                "id=" + id +
                ", payload=" + payload +
                '}';
    }
}
