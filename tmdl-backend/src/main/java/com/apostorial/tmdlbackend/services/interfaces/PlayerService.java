package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.dtos.player.DataPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.SearchPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlayerService {
    ProfilePlayerRequest findById(String playerId) throws EntityNotFoundException;
    ProfilePlayerRequest findByUsername(String username) throws EntityNotFoundException;
    List<ProfilePlayerRequest> findAll();
    List<DataPlayerRequest> findAllByClassicPoints();
    List<DataPlayerRequest> findAllByPlatformerPoints();
    List<SearchPlayerRequest> findAllStaff();
    List<Player> findAllByRegionId(String regionId);
    List<DataPlayerRequest> findAllRequestsByRegionId(String regionId);
    ProfilePlayerRequest getAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException;
    UpdatePlayerRequest updateAuthenticatedPlayer(UpdatePlayerRequest request) throws EntityNotFoundException, UnauthorizedException;
    ProfilePlayerRequest uploadAvatar(MultipartFile file) throws EntityNotFoundException, IOException, UnauthorizedException;
    byte[] getAvatar(String playerId) throws EntityNotFoundException, IOException;
    void addRegion(String regionId) throws EntityNotFoundException, UnauthorizedException;
}
