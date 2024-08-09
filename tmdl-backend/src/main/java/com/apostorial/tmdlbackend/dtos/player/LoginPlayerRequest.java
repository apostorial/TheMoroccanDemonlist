package com.apostorial.tmdlbackend.dtos.player;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginPlayerRequest {
    @NotNull(message = "Username is required.")
    private String username;
    @NotNull(message = "Password is required.")
    private String password;
}
