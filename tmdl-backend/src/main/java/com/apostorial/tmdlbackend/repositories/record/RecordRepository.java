package com.apostorial.tmdlbackend.repositories.record;

import com.apostorial.tmdlbackend.entities.record.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepository extends MongoRepository<Record, String> {
}
