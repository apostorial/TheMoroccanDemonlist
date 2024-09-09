package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.level.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.OrderLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.level.PlatformerLevelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/platformer-levels") @SecurityRequirement(name = "bearer-jwt")
public class StaffPlatformerLevelController {
    private final PlatformerLevelService platformerLevelService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerLevel> create(@RequestBody CreatePlatformerLevelRequest request) {
        try {
            PlatformerLevel level = platformerLevelService.create(request);
            return new ResponseEntity<>(level, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{levelId}")
    public ResponseEntity<PlatformerLevel> update(@PathVariable String levelId, @RequestBody UpdatePlatformerLevelRequest request) {
        try {
            PlatformerLevel level = platformerLevelService.update(levelId, request);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reorder")
    public void updateLevelRankings(@RequestBody List<OrderLevelRequest> reorderedLevels) {
        platformerLevelService.reorderLevels(reorderedLevels);
    }

    @DeleteMapping("/delete/{levelId}")
    public ResponseEntity<Void> delete(@PathVariable String levelId) {
        try {
            platformerLevelService.deleteById(levelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
