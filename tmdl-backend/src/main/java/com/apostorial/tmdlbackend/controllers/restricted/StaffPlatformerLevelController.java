package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.PlatformerLevelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/platformer-levels") @SecurityRequirement(name = "bearer-jwt")
public class StaffPlatformerLevelController {
    private final PlatformerLevelService platformerLevelService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerLevel> create(@RequestBody CreatePlatformerLevelRequest request) {
        PlatformerLevel level = platformerLevelService.create(request);
        return new ResponseEntity<>(level, HttpStatus.CREATED);
    }

    @PutMapping("/update/{levelId}")
    public ResponseEntity<PlatformerLevel> update(@PathVariable String levelId, @RequestBody UpdatePlatformerLevelRequest request) {
        try {
            PlatformerLevel level = platformerLevelService.update(levelId, request);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{levelId}")
    public ResponseEntity<Void> delete(@PathVariable String levelId) {
        try {
            platformerLevelService.deleteById(levelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
