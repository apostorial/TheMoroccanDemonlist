package com.apostorial.tmdlbackend.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION = 60;

    @Id
    private String id;
    @NotNull
    private String token;
    @DBRef @NotNull
    private Player player;
    private LocalDateTime expiryDate;

    public VerificationToken(String token, Player player) {
        this.token = token;
        this.player = player;
        this.expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
    }
}
