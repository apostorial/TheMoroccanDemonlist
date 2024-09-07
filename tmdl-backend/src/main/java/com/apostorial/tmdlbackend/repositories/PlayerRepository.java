package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByUsername(String username);
    Optional<Player> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Player> findAllByRegionId(String regionId);
    List<Player> findByIsStaffTrue();
}
