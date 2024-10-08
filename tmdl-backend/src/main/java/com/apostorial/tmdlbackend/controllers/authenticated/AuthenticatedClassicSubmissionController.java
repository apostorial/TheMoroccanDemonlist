package com.apostorial.tmdlbackend.controllers.authenticated;

import com.apostorial.tmdlbackend.dtos.submission.CreateClassicSubmissionRequest;
import com.apostorial.tmdlbackend.entities.submission.ClassicSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.submission.ClassicSubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @AllArgsConstructor @PreAuthorize("isAuthenticated()") @RequestMapping("/api/authenticated/classic-submissions") @SecurityRequirement(name = "bearer-jwt")
public class AuthenticatedClassicSubmissionController {
    private final ClassicSubmissionService classicSubmissionService;

    @PostMapping("/create")
    public ResponseEntity<ClassicSubmission> create(@RequestBody CreateClassicSubmissionRequest request) {
        try {
            ClassicSubmission submission = classicSubmissionService.create(request);
            return new ResponseEntity<>(submission, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClassicSubmission>> getMySubmissions() {
        try {
            List<ClassicSubmission> submissions = classicSubmissionService.findByAuthenticatedPlayer();
            return new ResponseEntity<>(submissions, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{submissionId}")
    public ResponseEntity<Void> delete(@PathVariable String submissionId) {
        try {
            classicSubmissionService.deleteById(submissionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
