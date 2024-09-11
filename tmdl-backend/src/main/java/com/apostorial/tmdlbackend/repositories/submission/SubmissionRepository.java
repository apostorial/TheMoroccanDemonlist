package com.apostorial.tmdlbackend.repositories.submission;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SubmissionRepository<T> extends MongoRepository<T, String> {
    List<T> findByPlayerId(String playerId);
}
