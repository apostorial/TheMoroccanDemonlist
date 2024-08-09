package com.apostorial.tmdlbackend.services.interfaces.record;

import com.apostorial.tmdlbackend.dtos.record.CreatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

public interface PlatformerRecordService extends RecordService<PlatformerRecord> {
    PlatformerRecord create(CreatePlatformerRecordRequest request) throws EntityNotFoundException;
    PlatformerRecord update(String recordId, UpdatePlatformerRecordRequest request) throws EntityNotFoundException;
}
