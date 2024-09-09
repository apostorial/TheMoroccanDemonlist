package com.apostorial.tmdlbackend.services.interfaces.level;

import com.apostorial.tmdlbackend.dtos.level.LevelCountRequest;
import com.apostorial.tmdlbackend.dtos.level.PlayerLevelRequest;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface LevelService<T> {
    T findById(String levelId) throws EntityNotFoundException;
    List<T> findAll(String type);
    void deleteById(String levelId) throws EntityNotFoundException;
    List<T> findByDifficulty(Difficulty difficulty);
    LevelCountRequest getLevelCount(String playerId);
    Optional<PlayerLevelRequest> findHardestLevel(String playerId) throws EntityNotFoundException;
    List<PlayerLevelRequest> findAllByPlayerId(String playerId);
}
