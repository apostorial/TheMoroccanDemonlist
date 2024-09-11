package com.apostorial.tmdlbackend.repositories.level;

import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Duration;

import java.util.List;

public interface ClassicLevelRepository extends LevelRepository<ClassicLevel> {
    List<ClassicLevel> findByDuration(Duration duration);
    List<ClassicLevel> findByFirstVictorId(String playerId);
}
