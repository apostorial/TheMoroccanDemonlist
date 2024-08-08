package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;

import java.net.URL;
import java.util.List;

public interface ClassicLevelService extends LevelService<ClassicLevel> {
    ClassicLevel create(String levelId, String name, String publisher, Difficulty difficulty, URL link, URL thumbnail, Duration duration, int minimumCompletion);
    List<ClassicLevel> findByDuration(String duration);
}
