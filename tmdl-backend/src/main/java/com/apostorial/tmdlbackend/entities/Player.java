package com.apostorial.tmdlbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document @Data @NoArgsConstructor @AllArgsConstructor
public class Player {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String avatar;
    private boolean isStaff = false;
    private boolean isActive = true;
    private LocalDateTime dateJoined = LocalDateTime.now();
    private Region region;
    private float classicPoints = 0;
    private float platformerPoints = 0;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;
}
