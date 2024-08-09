package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.record.ClassicRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @AllArgsConstructor @RequestMapping("/api/public/classic-records")
public class PublicClassicRecordController {
    private final ClassicRecordService classicRecordService;

    @GetMapping("/{recordId}")
    public ResponseEntity<ClassicRecord> findById(@PathVariable String recordId) {
        try {
            ClassicRecord record = classicRecordService.findById(recordId);
            return new ResponseEntity<>(record, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<ClassicRecord>> findAllByPlayerId(@PathVariable String playerId) {
        try {
            List<ClassicRecord> records = classicRecordService.findAllByPlayerId(playerId);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<ClassicRecord>> findAllByLevelId(@PathVariable String levelId) {
        try {
            List<ClassicRecord> records = classicRecordService.findAllByLevelId(levelId);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<ClassicRecord>> findAll() {
        try {
            List<ClassicRecord> records = classicRecordService.findAll();
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
