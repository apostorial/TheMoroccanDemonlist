package com.apostorial.tmdlbackend.controllers.restricted;

import com.apostorial.tmdlbackend.dtos.region.CreateRegionRequest;
import com.apostorial.tmdlbackend.dtos.region.UpdateRegionRequest;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.services.interfaces.RegionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor @PreAuthorize("hasRole('ROLE_STAFF')") @RequestMapping("/api/staff/regions") @SecurityRequirement(name = "bearer-jwt")
public class StaffRegionController {
    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<Region> create(@RequestBody CreateRegionRequest request) {
        try {
            Region region = regionService.create(request);
            return new ResponseEntity<>(region, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{regionId}")
    public ResponseEntity<Region> update(@PathVariable String regionId, @RequestBody UpdateRegionRequest request) {
        try {
            Region region = regionService.update(regionId, request);
            return new ResponseEntity<>(region, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{regionId}")
    public ResponseEntity<Void> delete(@PathVariable String regionId) {
        try {
            regionService.deleteById(regionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
