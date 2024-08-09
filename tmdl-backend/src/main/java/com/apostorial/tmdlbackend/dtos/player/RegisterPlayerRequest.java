package com.apostorial.tmdlbackend.dtos.player;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterPlayerRequest {
    @NotNull(message = "Username is required.")
    private String username;
    @NotNull(message = "Email is required.")
    private String email;
    @NotNull(message = "Password is required.")
    private String password;
    @NotNull(message = "Region ID is required.")
    private String regionId;
}
