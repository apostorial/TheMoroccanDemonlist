package com.apostorial.tmdlbackend.services.implementations.submission;


import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.submission.CreatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.submission.PlatformerSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.submission.PlatformerSubmissionRepository;
import com.apostorial.tmdlbackend.services.interfaces.submission.PlatformerSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class PlatformerSubmissionServiceImpl implements PlatformerSubmissionService {
    private final PlatformerSubmissionRepository platformerSubmissionRepository;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final SecurityUtils securityUtils;

    @Override
    public PlatformerSubmission create(CreatePlatformerSubmissionRequest request) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        PlatformerSubmission platformerSubmission = new PlatformerSubmission();
        platformerSubmission.setPlayer(player);
        platformerSubmission.setLink(request.getLink());
        platformerSubmission.setComment(request.getComment());
        if (request.getLevel() != null) {
            PlatformerLevel level = platformerLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevel() + " not found"));
            platformerSubmission.setLevel(level);
        }
        platformerSubmission.setRecordTime(request.getRecordTime());
        return platformerSubmissionRepository.save(platformerSubmission);
    }

    @Override
    public PlatformerSubmission update(String submissionId, UpdatePlatformerSubmissionRequest request) throws EntityNotFoundException {
        return null;
    }

    @Override
    public PlatformerSubmission findById(String submissionId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public List<PlatformerSubmission> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(String regionId) throws EntityNotFoundException {

    }
}
