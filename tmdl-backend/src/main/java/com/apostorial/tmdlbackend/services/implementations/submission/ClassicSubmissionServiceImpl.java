package com.apostorial.tmdlbackend.services.implementations.submission;


import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.submission.CreateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.submission.ClassicSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.submission.ClassicSubmissionRepository;
import com.apostorial.tmdlbackend.services.interfaces.submission.ClassicSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class ClassicSubmissionServiceImpl implements ClassicSubmissionService {
    private final ClassicSubmissionRepository classicSubmissionRepository;
    private final ClassicLevelRepository classicLevelRepository;
    private final SecurityUtils securityUtils;

    @Override
    public ClassicSubmission create(CreateClassicSubmissionRequest request) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        ClassicSubmission classicSubmission = new ClassicSubmission();
        classicSubmission.setPlayer(player);
        classicSubmission.setLink(request.getLink());
        classicSubmission.setComment(request.getComment());
        if (request.getLevel() != null) {
            ClassicLevel level = classicLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevel() + " not found"));
            classicSubmission.setLevel(level);
        }
        classicSubmission.setRecordPercentage(request.getRecordPercentage());
        return classicSubmissionRepository.save(classicSubmission);
    }

    @Override
    public ClassicSubmission update(String submissionId, UpdateClassicSubmissionRequest request) throws EntityNotFoundException {
        return null;
    }

    @Override
    public ClassicSubmission findById(String submissionId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<ClassicSubmission> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(String regionId) throws EntityNotFoundException {

    }
}
