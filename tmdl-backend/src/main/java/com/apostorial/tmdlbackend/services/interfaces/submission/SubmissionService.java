package com.apostorial.tmdlbackend.services.interfaces.submission;

import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface SubmissionService<T> {
    T findById(String submissionId) throws EntityNotFoundException;
    List<T> findAll();
    void deleteById(String regionId) throws EntityNotFoundException;
}
