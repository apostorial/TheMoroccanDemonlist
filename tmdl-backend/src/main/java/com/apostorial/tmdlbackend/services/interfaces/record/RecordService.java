package com.apostorial.tmdlbackend.services.interfaces.record;

import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordService<T> {
    T findById(String recordId) throws EntityNotFoundException;
    Page<T> findAll(Pageable pageable);
    List<T> findAllByPlayerId(String playerId);
    List<T> findAllByLevelId(String levelId);
    void deleteById(String levelId) throws EntityNotFoundException;
}
