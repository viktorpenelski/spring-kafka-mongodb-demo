package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.JsonstoreProducer;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;

public class JsonstoreControllerTest {

    @Test
    public void enqueue_json_gets_mapped_before_producing() throws IOException {

        // GIVEN
        JsonstoreProducer producer = Mockito.mock(JsonstoreProducer.class);
        JsonstoreController controller = new JsonstoreController(producer);

        // WHEN
        JsonNode jsonNode = new ObjectMapper()
                .readTree("{ \"key\": \"value\" }");
        controller.enqueueJson(jsonNode);

        // THEN
        Map<String, Object> expected = Map.of("key", "value");

        Mockito.verify(producer).sendMessage(eq(expected));
    }

    @Test
    public void enqueue_json_maps_lists_properly_before_producing() throws IOException {

        // GIVEN
        JsonstoreProducer producer = Mockito.mock(JsonstoreProducer.class);
        JsonstoreController controller = new JsonstoreController(producer);

        // WHEN
        JsonNode jsonNode = new ObjectMapper()
                .readTree("{ \"aList\": [\"first\", \"second\"] }");
        controller.enqueueJson(jsonNode);

        // THEN
        Map<String, Object> expected = Map.of("aList", List.of("first", "second"));

        Mockito.verify(producer).sendMessage(eq(expected));
    }
}