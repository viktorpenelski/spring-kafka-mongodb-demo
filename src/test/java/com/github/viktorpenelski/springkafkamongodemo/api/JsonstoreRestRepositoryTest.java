package com.github.viktorpenelski.springkafkamongodemo.api;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.JsonstoreProducer;
import com.github.viktorpenelski.springkafkamongodemo.jsonstore.producer.web.JsonstoreController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(JsonstoreController.class)
public class JsonstoreRestRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonstoreProducer producer;

    @Test
    public void successful_enqueue_returns_202_accepted() throws Exception {

        given(producer.sendMessage(any()))
                .willReturn(UUID.randomUUID());

        mockMvc.perform(post("/jsonstore/enqueue")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

    }

    @Test
    public void invalid_body_enqueue_returns_400_bad_request() throws Exception {

        given(producer.sendMessage(any()))
                .willReturn(UUID.randomUUID());

        mockMvc.perform(post("/jsonstore/enqueue")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("}{"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}