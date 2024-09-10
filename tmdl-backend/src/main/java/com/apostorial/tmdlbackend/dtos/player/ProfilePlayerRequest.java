package com.apostorial.tmdlbackend.dtos.player;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfilePlayerRequest {
    private String id;
    private String username;
    private LocalDateTime dateJoined;
    private String region;
    private Double classicPoints;
    private Double platformerPoints;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;
    private Boolean isStaff;
}
