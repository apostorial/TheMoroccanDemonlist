package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.player.LoginPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.RegisterPlayerRequest;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

public interface AuthService {
    String login(LoginPlayerRequest loginPlayerRequest);
    String register(RegisterPlayerRequest registerPlayerRequest) throws EntityNotFoundException;
    boolean verifyEmail(String token);
}
