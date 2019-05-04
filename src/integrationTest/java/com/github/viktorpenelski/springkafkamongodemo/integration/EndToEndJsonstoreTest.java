package com.github.viktorpenelski.springkafkamongodemo.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class EndToEndJsonstoreTest {

    private static final String BASE_URL =
            Optional.ofNullable(System.getenv("INTEGRATION_TEST_BASE_URL_PORT"))
                    .orElse("http://127.0.0.1:8080");
    private static final int OK_200 = 200;
    private static final int ACCEPTED_202 = 202;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Test
    public void enqueue_new_resource_and_retrieve_it() throws Exception {

        HttpResponse<String> enqueueResponse = enqueueResource();

        assertEquals(enqueueErrorMessage(enqueueResponse),
                ACCEPTED_202, enqueueResponse.statusCode());

        JsonPath path = JsonPath.compile("$.href");
        String resourceHref = path.read(enqueueResponse.body());

        String getErrorMessage = "Expected HttpStatus [200] but got [%d] when calling %s \n%s";

        lookupResource(resourceHref).ifPresentOrElse(getResponse -> assertEquals(
                String.format(getErrorMessage, getResponse.statusCode(), resourceHref, getResponse.body()),
                OK_200, getResponse.statusCode()),

                () -> Assert.fail(String.format("Failed to call %s after %d retries!", resourceHref, 2)));

    }

    private String enqueueErrorMessage(HttpResponse<String> enqueueResponse) {
        return String.format(
                "Expected HttpStatus [%d] but got [%d] \n%s\nwhen posting \n%s\n to %s",
                ACCEPTED_202, enqueueResponse.statusCode(), enqueueResponse.body(),
                enqueueResponse.body(), enqueueResponse.uri());
    }

    private HttpResponse<String> enqueueResource() throws IOException, InterruptedException {
        String url = BASE_URL + "/jsonstore/enqueue";
        String body = "{}";

        HttpRequest enqueueRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return HTTP_CLIENT.send(enqueueRequest, HttpResponse.BodyHandlers.ofString());
    }

    private Optional<HttpResponse<String>> lookupResource(String resourceHref) {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + resourceHref))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();


        RetriableClient retriableClient = new RetriableClient(HTTP_CLIENT);
        return retriableClient.send(getRequest, 2, 2000L);

    }
}
