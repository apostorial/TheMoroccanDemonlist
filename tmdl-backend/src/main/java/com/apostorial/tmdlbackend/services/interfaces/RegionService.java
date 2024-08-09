package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.region.CreateRegionRequest;
import com.apostorial.tmdlbackend.dtos.region.UpdateRegionRequest;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;

public interface RegionService {
    Region findById(String regionId) throws EntityNotFoundException;
    List<Region> findAll();
    Region create(CreateRegionRequest request);
    Region update(String regionId, UpdateRegionRequest request) throws EntityNotFoundException;
    void deleteById(String regionId) throws EntityNotFoundException;
}
