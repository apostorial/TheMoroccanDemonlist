package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepository extends MongoRepository<Record, String> {
}
