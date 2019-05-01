package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.JsonstoreProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jsonstore")
public class JsonstoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonstoreController.class);

    private JsonstoreProducer producer;

    public JsonstoreController(JsonstoreProducer producer) {
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
    public ResponseEntity<JsonstoreEnqueueResponse> enqueueJson(
            @Valid @RequestBody
                    JsonNode body) {

        LOGGER.info("POST /enqueue called with payload: {}", body.toString());
        UUID msgId = producer.sendMessage(mapFrom(body));

        return new ResponseEntity<>(JsonstoreEnqueueResponse.from(msgId), HttpStatus.ACCEPTED);
    }

    private Map<String, Object> mapFrom(JsonNode jsonNode) {
        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
        };
        ObjectMapper mapper = new ObjectMapper();

        return mapper.convertValue(jsonNode, typeRef);
    }

}
