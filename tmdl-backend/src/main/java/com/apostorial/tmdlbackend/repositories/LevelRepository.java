package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.enums.Difficulty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface LevelRepository<T> extends MongoRepository<T, String> {
    List<T> findByDifficulty(Difficulty difficulty);
}
