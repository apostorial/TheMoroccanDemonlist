package com.apostorial.tmdlbackend.repositories.submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;

@NoRepositoryBean
public interface SubmissionRepository<T> extends MongoRepository<T, String> {
    @NonNull
    Page<T> findAll(@NonNull Pageable pageable);
    List<T> findByPlayerId(String playerId);
}
