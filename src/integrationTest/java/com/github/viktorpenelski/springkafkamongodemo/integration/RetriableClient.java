package com.github.viktorpenelski.springkafkamongodemo.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Retriable wrapper of {@link HttpClient}.
 * <p>
 * Exceptions and responses with status code outside of 2xx will be retried.
 */
public class RetriableClient {

    private static final Logger logger = LoggerFactory.getLogger(RetriableClient.class);

    private HttpClient client;

    /**
     * @param client used to make the http requests.
     */
    public RetriableClient(HttpClient client) {
        this.client = client;
    }

    /**
     * @param request    - {@link HttpRequest} to be executed.
     * @param retries    - how many times to retry, before giving up.
     * @param retrySleep - time in milliseconds to wait after each failed attempt.
     * @return On success: Optional of the response.
     * On error (no successful send): an empty Optional
     */
    public Optional<HttpResponse<String>> send(HttpRequest request,
                                               int retries,
                                               long retrySleep) {

        HttpResponse<String> response = null;
        while (retries >= 0) {
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception ignored) {
            } finally {
                retries--;
            }

            if (response == null || response.statusCode() < 200 || response.statusCode() > 299) {
                logger.debug("Bad response, retrying...\nresponse: {}", response);
                waitUntilNextTry(retrySleep);
                continue;
            }

            return Optional.of(response);
        }

        return Optional.empty();
    }

    private void waitUntilNextTry(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ignored) {
        }
    }
}
