package com.github.viktorpenelski.springkafkamongodemo.jsonstore;

import com.github.viktorpenelski.springkafkamongodemo.jsonstore.model.JsonstoreRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface JsonstoreRepository extends MongoRepository<JsonstoreRecord, UUID> {

}
