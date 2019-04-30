package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.JsonProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jsonstore")
public class JsonController {

    private JsonProducer producer;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonController.class);

    public JsonController(JsonProducer producer) {
        this.producer = producer;
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
        UUID msgId = producer.sendMessage(body.toString());

        return new ResponseEntity<>(new JsonEnqueueResponse(msgId, body), HttpStatus.ACCEPTED);
    }

    @GetMapping()
    public ResponseEntity<List<JsonNode>> getAllJson() {

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

}
