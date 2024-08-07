package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
    VerificationToken findByPlayer(Player player);
}
