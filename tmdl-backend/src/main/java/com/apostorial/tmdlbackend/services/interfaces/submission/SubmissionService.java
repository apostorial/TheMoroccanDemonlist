package com.apostorial.tmdlbackend.services.interfaces.submission;

import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubmissionService<T> {
    T findById(String submissionId) throws EntityNotFoundException;
    List<T> findByPlayerId(String playerId) throws EntityNotFoundException;
    List<T> findByAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException;
    Page<T> findAll(Pageable pageable);
    void deleteById(String submissionId) throws EntityNotFoundException, UnauthorizedException;
    void changeStatus(String submissionId, Status status) throws EntityNotFoundException;
}
