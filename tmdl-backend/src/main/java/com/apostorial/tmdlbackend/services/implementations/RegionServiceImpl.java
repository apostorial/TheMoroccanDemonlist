package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.dtos.region.CreateRegionRequest;
import com.apostorial.tmdlbackend.dtos.region.UpdateRegionRequest;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.RegionRepository;
import com.apostorial.tmdlbackend.services.interfaces.RegionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class RegionServiceImpl implements RegionService {
    private RegionRepository regionRepository;

    @Override
    public Region create(CreateRegionRequest request) {
        Region region = new Region();
        region.setName(request.getName());
        return regionRepository.save(region);
    }

    @Override
    public Region findById(String regionId) throws EntityNotFoundException {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region with id " + regionId + " not found"));
    }

    @Override
    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    @Override
    public Region update(String regionId, UpdateRegionRequest request) throws EntityNotFoundException {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region with id " + regionId + " not found"));
        region.setName(request.getName());
        return regionRepository.save(region);
    }

    @Override
    public void deleteById(String regionId) throws EntityNotFoundException {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region with id " + regionId + " not found"));
        regionRepository.delete(region);
    }
}
