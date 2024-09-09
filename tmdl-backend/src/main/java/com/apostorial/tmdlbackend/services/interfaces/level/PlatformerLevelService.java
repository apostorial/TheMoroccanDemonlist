package com.apostorial.tmdlbackend.services.interfaces.level;

import com.apostorial.tmdlbackend.dtos.level.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.PlayerLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface PlatformerLevelService extends LevelService<PlatformerLevel> {
    PlatformerLevel create(CreatePlatformerLevelRequest request);
    PlatformerLevel update(String levelId, UpdatePlatformerLevelRequest request) throws EntityNotFoundException;
    List<PlayerLevelRequest> findByRecordHolder(String playerId);
}
