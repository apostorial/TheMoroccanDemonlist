package com.apostorial.tmdlbackend.repositories.record;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;

@NoRepositoryBean
public interface RecordRepository<T> extends MongoRepository<T, String> {
    @NonNull
    Page<T> findAll(@NonNull Pageable pageable);
    List<T> findAllByPlayerId(String playerId);
    List<T> findAllByLevelId(String levelId);
    boolean existsByPlayerIdAndLevelId(String playerId, String levelId);
    void deleteByLevelId(String levelId);
}
