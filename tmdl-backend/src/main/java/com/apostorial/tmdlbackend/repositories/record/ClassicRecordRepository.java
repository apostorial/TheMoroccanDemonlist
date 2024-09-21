package com.apostorial.tmdlbackend.repositories.record;

import com.apostorial.tmdlbackend.entities.record.ClassicRecord;

import java.util.Optional;

public interface ClassicRecordRepository extends RecordRepository<ClassicRecord> {
    Optional<ClassicRecord> findByLevelIdAndPlayerId(String levelId, String playerId);
}
