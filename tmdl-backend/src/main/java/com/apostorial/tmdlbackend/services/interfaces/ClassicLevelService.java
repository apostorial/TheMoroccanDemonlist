package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.level.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface ClassicLevelService extends LevelService<ClassicLevel> {
    ClassicLevel create(CreateClassicLevelRequest request);
    List<ClassicLevel> findByDuration(Duration duration);
    ClassicLevel update(String levelId, UpdateClassicLevelRequest request) throws EntityNotFoundException;
}
