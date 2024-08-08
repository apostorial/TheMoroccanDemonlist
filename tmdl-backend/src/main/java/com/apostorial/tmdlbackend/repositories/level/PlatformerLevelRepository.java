package com.apostorial.tmdlbackend.repositories.level;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;

import java.util.List;

public interface PlatformerLevelRepository extends LevelRepository<PlatformerLevel> {
    List<PlatformerLevel> findByDifficulty(Difficulty difficulty);
}
