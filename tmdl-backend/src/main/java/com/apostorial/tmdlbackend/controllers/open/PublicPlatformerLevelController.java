package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.level.PlatformerLevelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @AllArgsConstructor @RequestMapping("/api/public/platformer-levels")
public class PublicPlatformerLevelController {
    private final PlatformerLevelService platformerLevelService;

    @GetMapping("/{levelId}")
    public ResponseEntity<PlatformerLevel> findById(@PathVariable String levelId) {
        try {
            PlatformerLevel level = platformerLevelService.findByLevelId(levelId);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<PlatformerLevel>> findByDifficulty(@PathVariable Difficulty difficulty) {
        try {
            List<PlatformerLevel> levels = platformerLevelService.findByDifficulty(difficulty);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/list", "/list/{type}"})
    public ResponseEntity<List<PlatformerLevel>> findAll(@PathVariable(required = false) String type) {
        try {
            List<PlatformerLevel> levels = platformerLevelService.findAll(type);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
