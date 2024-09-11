package com.apostorial.tmdlbackend.services.interfaces.submission;

import com.apostorial.tmdlbackend.dtos.submission.CreateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.entities.submission.ClassicSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;

public interface ClassicSubmissionService extends SubmissionService<ClassicSubmission> {
    ClassicSubmission create(CreateClassicSubmissionRequest request) throws EntityNotFoundException, UnauthorizedException;
    ClassicSubmission update(String submissionId, UpdateClassicSubmissionRequest request) throws EntityNotFoundException;
}
