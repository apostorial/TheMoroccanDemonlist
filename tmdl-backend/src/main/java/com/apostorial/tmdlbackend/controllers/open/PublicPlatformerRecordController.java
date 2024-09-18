package com.apostorial.tmdlbackend.controllers.open;

import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @AllArgsConstructor @RequestMapping("/api/public/platformer-records")
public class PublicPlatformerRecordController {
    private final PlatformerRecordService platformerRecordService;

    @GetMapping("/{recordId}")
    public ResponseEntity<PlatformerRecord> findById(@PathVariable String recordId) {
        try {
            PlatformerRecord record = platformerRecordService.findById(recordId);
            return new ResponseEntity<>(record, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<PlatformerRecord>> findAllByPlayer(@PathVariable String playerId) {
        try {
            List<PlatformerRecord> records = platformerRecordService.findAllByPlayerId(playerId);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<PlatformerRecord>> findAllByLevelId(@PathVariable String levelId) {
        try {
            List<PlatformerRecord> records = platformerRecordService.findAllByLevelId(levelId);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<Page<PlatformerRecord>> findAll(Pageable pageable) {
        try {
            Page<PlatformerRecord> records = platformerRecordService.findAll(pageable);
            if (records.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
