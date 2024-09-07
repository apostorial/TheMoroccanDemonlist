package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.dtos.level.LevelCountRequest;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.level.ClassicLevelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController @AllArgsConstructor @RequestMapping("/api/public/classic-levels")
public class PublicClassicLevelController {
    private final ClassicLevelService classicLevelService;

    @GetMapping("/{levelId}")
    public ResponseEntity<ClassicLevel> findByLevelId(@PathVariable String levelId) {
        try {
            ClassicLevel level = classicLevelService.findByLevelId(levelId);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/duration/{duration}")
    public ResponseEntity<List<ClassicLevel>> findByDuration(@PathVariable Duration duration) {
        try {
            List<ClassicLevel> levels = classicLevelService.findByDuration(duration);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<ClassicLevel>> findByDifficulty(@PathVariable Difficulty difficulty) {
        try {
            List<ClassicLevel> levels = classicLevelService.findByDifficulty(difficulty);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/firstVictor/{playerId}")
    public ResponseEntity<List<ClassicLevel>> findByFirstVictor(@PathVariable String playerId) {
        try {
            List<ClassicLevel> levels = classicLevelService.findByFirstVictor(playerId);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hardestLevel/{playerId}")
    public ResponseEntity<Optional<ClassicLevel>> findHardestLevel(@PathVariable String playerId) {
        try {
            Optional<ClassicLevel> level = classicLevelService.findHardestLevel(playerId);
            return new ResponseEntity<>(level, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/{playerId}")
    public ResponseEntity<LevelCountRequest> getLevelCount(@PathVariable String playerId) {
        return new ResponseEntity<>(classicLevelService.getLevelCount(playerId), HttpStatus.OK);
    }

    @GetMapping({"/list", "/list/{type}"})
    public ResponseEntity<List<ClassicLevel>> findAll(@PathVariable(required = false) String type) {
        try {
            List<ClassicLevel> levels = classicLevelService.findAll(type);
            if (levels.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(levels, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
