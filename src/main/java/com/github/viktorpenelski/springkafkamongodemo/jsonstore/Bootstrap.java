package com.github.viktorpenelski.springkafkamongodemo.jsonstore;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Bootstrap {

    private String topicName;
    private int numPartitions;
    private short replicationFactor;

    public Bootstrap(@Value("${jsonstore.kafka.topic}")
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
     * Initialize the jsonstore topic if it does not already exist.
     */
    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(topicName, numPartitions, replicationFactor);
    }
}
