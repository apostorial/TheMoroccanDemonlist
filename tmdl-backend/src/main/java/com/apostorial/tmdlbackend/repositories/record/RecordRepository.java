package com.apostorial.tmdlbackend.repositories.record;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface RecordRepository<T> extends MongoRepository<T, String> {
    List<T> findAllByPlayerId(String playerId);
    List<T> findAllByLevelId(String levelId);
}
