package com.apostorial.tmdlbackend.services.interfaces.record;

import com.apostorial.tmdlbackend.dtos.record.CreateClassicRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdateClassicRecordRequest;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

public interface ClassicRecordService extends RecordService<ClassicRecord> {
    ClassicRecord create(CreateClassicRecordRequest request) throws EntityNotFoundException;
    ClassicRecord update(String recordId, UpdateClassicRecordRequest request) throws EntityNotFoundException;
}
