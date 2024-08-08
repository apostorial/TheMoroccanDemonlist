package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassicLevelRepository extends MongoRepository<ClassicLevel, String> {
}
