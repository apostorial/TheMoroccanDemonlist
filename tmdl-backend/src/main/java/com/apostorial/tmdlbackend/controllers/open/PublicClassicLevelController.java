package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.ClassicLevelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @AllArgsConstructor @RequestMapping("/api/public/classic-levels")
public class PublicClassicLevelController {
    private final ClassicLevelService classicLevelService;

    @GetMapping("/{levelId}")
    public ResponseEntity<ClassicLevel> findById(@PathVariable String levelId) {
        try {
            ClassicLevel classicLevel = classicLevelService.findById(levelId);
            return new ResponseEntity<>(classicLevel, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
