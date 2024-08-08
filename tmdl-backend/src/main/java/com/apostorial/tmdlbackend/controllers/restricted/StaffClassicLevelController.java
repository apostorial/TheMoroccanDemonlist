package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.level.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.ClassicLevelService;
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
        ClassicLevel level = classicLevelService.create(request);
        return new ResponseEntity<>(level, HttpStatus.CREATED);
    }

    @PutMapping("/update/{levelId}")
    public ResponseEntity<ClassicLevel> update(@PathVariable String levelId, @RequestBody UpdateClassicLevelRequest request) {
        try {
            ClassicLevel level = classicLevelService.update(levelId, request);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
