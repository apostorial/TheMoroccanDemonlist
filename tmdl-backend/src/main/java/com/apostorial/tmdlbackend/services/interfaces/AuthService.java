package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.LoginRequest;
import com.apostorial.tmdlbackend.dtos.RegisterRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
    boolean verifyEmail(String token);
}
