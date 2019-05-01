package com.github.viktorpenelski.springkafkamongodemo.jsonstore;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaBootstrap {

    private String topicName;
    private int numPartitions;
    private short replicationFactor;

    public KafkaBootstrap(@Value("${jsonstore.kafka.topic}")
                             String topicName,
                          @Value("${jsonstore.kafka.numPartitions:1}")
                             int numPartitions,
                          @Value("${jsonstore.kafka.replicationFactor:1}")
                             short replicationFactor) {

        this.topicName = topicName;
        this.numPartitions = numPartitions;
        this.replicationFactor = replicationFactor;
    }

    /**
     * Initialize the jsonstore.kafka.topic topic in kafka if it does not already exist.
     */
    @Bean
    public NewTopic createJsonstoreTopic() {
        return new NewTopic(topicName, numPartitions, replicationFactor);
    }
}
