package com.apostorial.tmdlbackend.dtos.player;

import lombok.Data;

@Data
public class UpdatePlayerRequest {
    private String username;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;
}
