package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Level;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LevelRepository extends MongoRepository<Level, String> {
}
