package com.apostorial.tmdlbackend.services.interfaces.submission;

import com.apostorial.tmdlbackend.dtos.submission.CreatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.entities.submission.PlatformerSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;

public interface PlatformerSubmissionService extends SubmissionService<PlatformerSubmission> {
    PlatformerSubmission create(CreatePlatformerSubmissionRequest request) throws EntityNotFoundException, UnauthorizedException;
    PlatformerSubmission update(String submissionId, UpdatePlatformerSubmissionRequest request) throws EntityNotFoundException;
}
