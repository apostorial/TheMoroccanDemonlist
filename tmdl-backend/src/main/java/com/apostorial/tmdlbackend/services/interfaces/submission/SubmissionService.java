package com.apostorial.tmdlbackend.services.interfaces.submission;

import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;

import java.util.List;

public interface SubmissionService<T> {
    T findById(String submissionId) throws EntityNotFoundException;
    List<T> findByPlayerId(String playerId) throws EntityNotFoundException;
    List<T> findByAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException;
    List<T> findAll();
    void deleteById(String submissionId) throws EntityNotFoundException, UnauthorizedException;
    void changeStatus(String submissionId, Status status) throws EntityNotFoundException;
}
