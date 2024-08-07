package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.dtos.LoginDTO;
import com.apostorial.tmdlbackend.dtos.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
    boolean verifyEmail(String token);
}
