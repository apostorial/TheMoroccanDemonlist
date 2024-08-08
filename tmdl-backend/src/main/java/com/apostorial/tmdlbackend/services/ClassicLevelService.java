package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.dtos.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.dtos.UpdateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface ClassicLevelService extends LevelService<ClassicLevel> {
    ClassicLevel create(CreateClassicLevelRequest request);
    List<ClassicLevel> findByDuration(Duration duration);
    ClassicLevel update(String levelId, UpdateClassicLevelRequest request) throws EntityNotFoundException;
}
