package com.github.viktorpenelski.springkafkamongodemo.jsonstore.consumer.web;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "/jsonstore")
public interface JsonstoreRepository extends MongoRepository<JsonstoreRecord, UUID> {


    @RestResource
    Page<JsonstoreRecord> findAll(Pageable pageable);

    @RestResource
    Optional<JsonstoreRecord> findById(UUID uuid);
}
