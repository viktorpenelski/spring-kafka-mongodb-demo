package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.JsonstoreRepository;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.JsonProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jsonstore")
public class JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonController.class);

    private JsonProducer producer;
    private JsonstoreRepository repository;

    public JsonController(JsonProducer producer, JsonstoreRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    /**
     * Enqueue a JSON to be published.
     *
     * @param body of the JSON message.
     * @return 202 Accepted on successful publication.
     * 400 Bad Request if presented with an invalid JSON.
     */
    @PostMapping("/enqueue")
    public ResponseEntity<JsonEnqueueResponse> enqueueJson(
            @Valid @RequestBody JsonNode body) {

        LOGGER.info("POST /enqueue called with payload: {}", body.toString());
        UUID msgId = producer.sendMessage(mapFrom(body));

        return new ResponseEntity<>(new JsonEnqueueResponse(msgId, body), HttpStatus.ACCEPTED);
    }

    private Map<String, Object> mapFrom(JsonNode jsonNode) {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(jsonNode, typeRef);
    }

    @GetMapping()
    public ResponseEntity<List<JsonstoreRecord>> getAllJson() {

        List<JsonstoreRecord> all = repository.findAll();

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

}
