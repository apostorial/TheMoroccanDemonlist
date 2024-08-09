package com.apostorial.tmdlbackend.dtos.player;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePlayerRequest {
    @NotNull(message = "Username is required.")
    private String username;
    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;
}
