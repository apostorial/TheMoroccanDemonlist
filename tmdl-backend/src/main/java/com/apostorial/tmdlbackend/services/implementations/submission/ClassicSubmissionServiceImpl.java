package com.apostorial.tmdlbackend.services.implementations.submission;


import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.submission.CreateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.submission.ClassicSubmission;
import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.submission.ClassicSubmissionRepository;
import com.apostorial.tmdlbackend.services.interfaces.submission.ClassicSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        return classicSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Classic submission with id " + submissionId + " not found"));
    }

    @Override
    public List<ClassicSubmission> findByPlayerId(String playerId) {
        return classicSubmissionRepository.findByPlayerId(playerId);
    }

    @Override
    public List<ClassicSubmission> findByAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        return classicSubmissionRepository.findByPlayerId(player.getId());
    }

    @Override
    public List<ClassicSubmission> findAll() {
        return classicSubmissionRepository.findAll();
    }

    @Override
    public void changeStatus(String submissionId, Status status) throws EntityNotFoundException {
        ClassicSubmission submission = classicSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Classic submission with id " + submissionId + " not found"));
        submission.setStatus(status);
        classicSubmissionRepository.save(submission);
    }

    @Override
    public void deleteById(String submissionId) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        ClassicSubmission submission = classicSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Classic submission with id " + submissionId + " not found"));
        if (Objects.equals(player.getId(), submission.getPlayer().getId()) || player.isStaff()) {
            classicSubmissionRepository.delete(submission);
        } else {
            throw new UnauthorizedException("You are not allowed to delete this classic submission");
        }
    }
}
