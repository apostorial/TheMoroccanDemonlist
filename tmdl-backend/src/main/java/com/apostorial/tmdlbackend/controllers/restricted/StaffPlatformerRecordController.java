package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.record.CreatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('STAFF')") @RequestMapping("/api/staff/platformer-records") @SecurityRequirement(name = "bearer-jwt")
public class StaffPlatformerRecordController {
    private final PlatformerRecordService platformerRecordService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerRecord> create(@RequestBody CreatePlatformerRecordRequest request) {
        try {
            PlatformerRecord record = platformerRecordService.create(request);
            return new ResponseEntity<>(record, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{recordId}")
    public ResponseEntity<PlatformerRecord> update(@PathVariable String recordId, @RequestBody UpdatePlatformerRecordRequest request) {
        try {
            PlatformerRecord record = platformerRecordService.update(recordId, request);
            return new ResponseEntity<>(record, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> delete(@PathVariable String recordId) {
        try {
            platformerRecordService.deleteById(recordId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
