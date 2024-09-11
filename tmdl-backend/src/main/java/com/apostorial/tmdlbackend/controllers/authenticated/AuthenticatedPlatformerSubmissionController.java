package com.apostorial.tmdlbackend.controllers.authenticated;

import com.apostorial.tmdlbackend.dtos.submission.CreatePlatformerSubmissionRequest;
import com.apostorial.tmdlbackend.entities.submission.PlatformerSubmission;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.submission.PlatformerSubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @AllArgsConstructor @PreAuthorize("isAuthenticated()") @RequestMapping("/api/authenticated/platformer-submissions") @SecurityRequirement(name = "bearer-jwt")
public class AuthenticatedPlatformerSubmissionController {
    private final PlatformerSubmissionService platformerSubmissionService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerSubmission> create(@RequestBody CreatePlatformerSubmissionRequest request) {
        try {
            PlatformerSubmission submission = platformerSubmissionService.create(request);
            return new ResponseEntity<>(submission, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
