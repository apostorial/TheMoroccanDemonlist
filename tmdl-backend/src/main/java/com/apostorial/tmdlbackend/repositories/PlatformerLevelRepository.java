package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;

import java.util.List;

public interface PlatformerLevelRepository extends LevelRepository<PlatformerLevel> {
    List<PlatformerLevel> findByDifficulty(Difficulty difficulty);
}
