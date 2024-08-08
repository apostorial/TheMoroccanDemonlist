package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.ClassicLevelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/classic-levels") @SecurityRequirement(name = "bearer-jwt")
public class StaffClassicLevelController {
    private final ClassicLevelService classicLevelService;

    @PostMapping("/create")
    public ResponseEntity<ClassicLevel> create(@RequestBody CreateClassicLevelRequest request) {

        ClassicLevel level = classicLevelService.create(
            request.getLevelId(),
            request.getName(),
            request.getPublisher(),
            request.getDifficulty(),
            request.getLink(),
            request.getThumbnail(),
            request.getDuration(),
            request.getMinimumCompletion()
        );
        return new ResponseEntity<>(level, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{levelId}")
    public ResponseEntity<Void> delete(@PathVariable String levelId) {
        try {
            classicLevelService.deleteById(levelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
