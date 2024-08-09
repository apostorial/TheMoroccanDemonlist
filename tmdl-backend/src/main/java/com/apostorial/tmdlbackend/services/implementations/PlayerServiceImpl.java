package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.mappers.ProfilePlayerMapper;
import com.apostorial.tmdlbackend.mappers.UpdatePlayerMapper;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository playerRepository;
    private SecurityUtils securityUtils;
    private ProfilePlayerMapper profilePlayerMapper;
    private UpdatePlayerMapper updatePlayerMapper;

    @Override
    public Player findById(String playerId) throws EntityNotFoundException {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player with id " + playerId + " not found"));
    }

    @Override
    public Player findByUsername(String username) throws EntityNotFoundException {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player with username " + username + " not found"));
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> findAllByRegionId(String regionId) {
        return playerRepository.findAllByRegionId(regionId);
    }

    @Override
    public ProfilePlayerRequest getAuthenticatedPlayer() throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        return profilePlayerMapper.toDTO(player);
    }

    @Override
    public UpdatePlayerRequest updateAuthenticatedPlayer(UpdatePlayerRequest request) throws EntityNotFoundException, UnauthorizedException {
        try {
            Player player = securityUtils.getAuthenticatedPlayer();
            player.setUsername(request.getUsername());
            player.setDiscord(request.getDiscord());
            player.setYoutube(request.getYoutube());
            player.setTwitter(request.getTwitter());
            player.setTwitch(request.getTwitch());
            playerRepository.save(player);
            return updatePlayerMapper.toDTO(player);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Player with id " + request.getUsername() + " not found");
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("You are not authorized to update this player");
        }
    }
}
