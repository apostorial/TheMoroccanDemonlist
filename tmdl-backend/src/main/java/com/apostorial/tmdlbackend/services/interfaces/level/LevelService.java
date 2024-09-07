package com.apostorial.tmdlbackend.services.interfaces.level;

import com.apostorial.tmdlbackend.dtos.level.LevelCountRequest;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface LevelService<T> {
    T findByLevelId(String levelId) throws EntityNotFoundException;
    List<T> findAll(String type);
    void deleteById(String levelId) throws EntityNotFoundException;
    List<T> findByDifficulty(Difficulty difficulty);
    LevelCountRequest getLevelCount(String playerId);
}
