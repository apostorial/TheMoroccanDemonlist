package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;

import java.util.List;

public interface ClassicLevelRepository extends LevelRepository<ClassicLevel> {
    List<ClassicLevel> findByDuration(Duration duration);
    List<ClassicLevel> findByDifficulty(Difficulty difficulty);
}
