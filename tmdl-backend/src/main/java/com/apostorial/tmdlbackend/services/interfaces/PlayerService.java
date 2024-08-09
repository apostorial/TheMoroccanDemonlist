package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;

import java.util.List;

public interface PlayerService {
    Player findById(String playerId) throws EntityNotFoundException;
    Player findByUsername(String username) throws EntityNotFoundException;
    List<Player> findAll();
    List<Player> findAllByRegionId(String regionId);
    ProfilePlayerRequest getAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException;
    UpdatePlayerRequest updateAuthenticatedPlayer(UpdatePlayerRequest request) throws EntityNotFoundException, UnauthorizedException;
}
