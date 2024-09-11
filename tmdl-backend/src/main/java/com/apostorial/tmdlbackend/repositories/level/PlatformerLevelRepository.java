package com.apostorial.tmdlbackend.repositories.level;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;

import java.util.List;

public interface PlatformerLevelRepository extends LevelRepository<PlatformerLevel> {
    List<PlatformerLevel> findByRecordHolderId(String playerId);
}
