package com.apostorial.tmdlbackend.dtos.player;

import com.apostorial.tmdlbackend.entities.Region;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfilePlayerRequest {
    private String id;
    private String username;
    private LocalDateTime dateJoined;
    private Region region;
    private float classicPoints;
    private float platformerPoints;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;
    private Boolean isStaff;
}
