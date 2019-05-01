package com.github.viktorpenelski.springkafkamongodemo.jsonstore.web;


import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.web.JsonstoreEnqueueResponse;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonstoreEnqueueResponseTest {

    @Test
    public void create_from_id_all_fields_valid() {
        // GIVEN
        UUID id = UUID.randomUUID();

        // WHEN
        JsonstoreEnqueueResponse actual = JsonstoreEnqueueResponse.from(id);

        // THEN
        assertNotNull(actual);
        assertNotNull(actual.getMessage());
        assertEquals(id, actual.getId());
        assertEquals("/jsonstore/" + id, actual.getHref());
    }
}