package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByUsername(String username);
    Player findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
