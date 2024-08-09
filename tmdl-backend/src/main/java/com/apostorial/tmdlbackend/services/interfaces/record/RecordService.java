package com.apostorial.tmdlbackend.services.interfaces.record;

import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface RecordService<T> {
    T findById(String levelId) throws EntityNotFoundException;
    List<T> findAll();
    List<T> findAllByPlayerId(String playerId);
    List<T> findAllByLevelId(String levelId);
    void deleteById(String levelId) throws EntityNotFoundException;
}
