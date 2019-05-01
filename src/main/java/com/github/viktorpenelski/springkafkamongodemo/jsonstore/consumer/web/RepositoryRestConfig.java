package com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    /**
     * Disable the REST exposure for @RepositoryRestResource interfaces.
     * <p>
     * In order to enable desired methods to be exposed as REST resources,
     * annotate them with @RestResource.
     * <p>
     * For example:
     *
     * @RestResource Optional<JsonstoreRecord> findById(UUID uuid);
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration restConfig) {
        restConfig.disableDefaultExposure();
    }
}