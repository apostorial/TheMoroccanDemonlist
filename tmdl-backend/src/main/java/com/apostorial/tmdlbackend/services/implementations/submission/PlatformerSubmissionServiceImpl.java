package com.apostorial.tmdlbackend.services.implementations.submission;


import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.record.CreatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.dtos.submission.CreatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.dtos.submission.UpdatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.submission.PlatformerSubmission;
import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.DuplicateRecordException;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.submission.PlatformerSubmissionRepository;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import com.apostorial.tmdlbackend.services.interfaces.submission.PlatformerSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service @AllArgsConstructor
public class PlatformerSubmissionServiceImpl implements PlatformerSubmissionService {
    private final PlatformerSubmissionRepository platformerSubmissionRepository;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final SecurityUtils securityUtils;
    private final PlatformerRecordService platformerRecordService;

    @Override
    public PlatformerSubmission create(CreatePlatformerSubmissionRequest request) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        PlatformerSubmission platformerSubmission = new PlatformerSubmission();
        platformerSubmission.setPlayer(player);
        platformerSubmission.setLink(request.getLink());
        platformerSubmission.setRawFootage(request.getRawFootage());
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
        return platformerSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer submission with id " + submissionId + " not found"));
    }

    @Override
    public List<PlatformerSubmission> findByPlayerId(String playerId) {
        return platformerSubmissionRepository.findByPlayerId(playerId);
    }

    @Override
    public List<PlatformerSubmission> findByAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        return platformerSubmissionRepository.findByPlayerId(player.getId());
    }

    @Override
    public Page<PlatformerSubmission> findAll(Pageable pageable) {
        return platformerSubmissionRepository.findAll(pageable);
    }

    @Override
    public void changeStatus(String submissionId, Status status) throws EntityNotFoundException, DuplicateRecordException {
        PlatformerSubmission submission = platformerSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer submission with id " + submissionId + " not found"));
        submission.setStatus(status);
        platformerSubmissionRepository.save(submission);

        if (status == Status.APPROVED) {
            try {
                CreatePlatformerRecordRequest recordRequest = new CreatePlatformerRecordRequest();
                recordRequest.setPlayer(submission.getPlayer().getId());
                recordRequest.setLevel(submission.getLevel().getId());
                recordRequest.setLink(submission.getLink());
                recordRequest.setRecordTime(submission.getRecordTime());
                platformerRecordService.create(recordRequest);
            } catch (DuplicateRecordException e) {
                throw new DuplicateRecordException("A record for this player and level already exists");
            }
        }
    }

    @Override
    public void deleteById(String submissionId) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        PlatformerSubmission submission = platformerSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer submission with id " + submissionId + " not found"));
        if (Objects.equals(player.getId(), submission.getPlayer().getId()) || player.isStaff()) {
            platformerSubmissionRepository.delete(submission);
        } else {
            throw new UnauthorizedException("You are not allowed to delete this platformer submission");
        }
    }
}
