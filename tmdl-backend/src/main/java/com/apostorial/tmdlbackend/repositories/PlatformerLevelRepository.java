package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlatformerLevelRepository extends MongoRepository<PlatformerLevel, String> {
}
