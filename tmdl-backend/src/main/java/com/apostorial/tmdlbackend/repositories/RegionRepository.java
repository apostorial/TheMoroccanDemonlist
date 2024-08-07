package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionRepository extends MongoRepository<Region, String> {
}
