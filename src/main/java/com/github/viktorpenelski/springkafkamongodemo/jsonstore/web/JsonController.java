package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/jsonstore")
public class JsonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonController.class);

    @PostMapping("/enqueue")
    public ResponseEntity<JsonNode> enqueueJson(
            @Valid @RequestBody JsonNode body) {

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<JsonNode>> getAllJson() {

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }

}
