package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.entities.submission.PlatformerSubmission;
import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.DuplicateRecordException;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.submission.PlatformerSubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/platformer-submissions") @SecurityRequirement(name = "bearer-jwt")
public class StaffPlatformerSubmissionController {
    private final PlatformerSubmissionService platformerSubmissionService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<PlatformerSubmission> findById(@PathVariable String submissionId) {
        try {
            PlatformerSubmission submission = platformerSubmissionService.findById(submissionId);
            return new ResponseEntity<>(submission, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<PlatformerSubmission>> findByPlayerId(@PathVariable String playerId) {
        try {
            List<PlatformerSubmission> submissions = platformerSubmissionService.findByPlayerId(playerId);
            if (submissions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(submissions, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PlatformerSubmission>> findAll(Pageable pageable) {
        try {
            Page<PlatformerSubmission> submissions = platformerSubmissionService.findAll(pageable);
            if (submissions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(submissions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{submissionId}/change-status/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable String submissionId, @PathVariable Status status) {
        try {
            platformerSubmissionService.changeStatus(submissionId, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DuplicateRecordException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
