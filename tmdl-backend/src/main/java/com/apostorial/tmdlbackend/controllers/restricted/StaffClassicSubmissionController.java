package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.entities.submission.ClassicSubmission;
import com.apostorial.tmdlbackend.enums.Status;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.submission.ClassicSubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/classic-submissions") @SecurityRequirement(name = "bearer-jwt")
public class StaffClassicSubmissionController {
    private final ClassicSubmissionService classicSubmissionService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<ClassicSubmission> findById(@PathVariable String submissionId) {
        try {
            ClassicSubmission submission = classicSubmissionService.findById(submissionId);
            return new ResponseEntity<>(submission, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<ClassicSubmission>> findByPlayerId(@PathVariable String playerId) {
        try {
            List<ClassicSubmission> submissions = classicSubmissionService.findByPlayerId(playerId);
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
    public ResponseEntity<List<ClassicSubmission>> findAll() {
        try {
            List<ClassicSubmission> submissions = classicSubmissionService.findAll();
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
            classicSubmissionService.changeStatus(submissionId, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
