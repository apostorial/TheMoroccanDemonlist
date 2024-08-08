package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.dtos.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

public interface PlatformerLevelService extends LevelService<PlatformerLevel> {
    PlatformerLevel create(CreatePlatformerLevelRequest request);
    PlatformerLevel update(String levelId, UpdatePlatformerLevelRequest request) throws EntityNotFoundException;
}
