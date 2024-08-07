package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.dtos.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
}
