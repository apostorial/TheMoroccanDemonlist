package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.config.SecurityUtils;
import com.apostorial.tmdlbackend.dtos.player.ProfilePlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.SearchPlayerRequest;
import com.apostorial.tmdlbackend.dtos.player.UpdatePlayerRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.exceptions.UnauthorizedException;
import com.apostorial.tmdlbackend.mappers.ProfilePlayerMapper;
import com.apostorial.tmdlbackend.mappers.SearchPlayerMapper;
import com.apostorial.tmdlbackend.mappers.UpdatePlayerMapper;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.RegionRepository;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final RegionRepository regionRepository;
    private final SecurityUtils securityUtils;
    private final ProfilePlayerMapper profilePlayerMapper;
    private final UpdatePlayerMapper updatePlayerMapper;
    private final SearchPlayerMapper searchPlayerMapper;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    private static final long MAX_FILE_SIZE = 1024 * 1024;

    @Override
    public ProfilePlayerRequest findById(String playerId) throws EntityNotFoundException {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player with id " + playerId + " not found"));
        return profilePlayerMapper.toDTO(player);
    }

    @Override
    public ProfilePlayerRequest findByUsername(String username) throws EntityNotFoundException {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player with username " + username + " not found"));
        return profilePlayerMapper.toDTO(player);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> findAllByClassicPoints() {
        return playerRepository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(Player::getClassicPoints).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Player> findAllByPlatformerPoints() {
        return playerRepository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(Player::getPlatformerPoints).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchPlayerRequest> findAllStaff() {
        return playerRepository.findByIsStaffTrue().stream()
                .map(searchPlayerMapper::toDTO)
                .toList();
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

    @Override
    public ProfilePlayerRequest uploadAvatar(MultipartFile file) throws EntityNotFoundException, IOException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();

        if (player == null) {
            throw new UnauthorizedException("You must be logged in to upload a profile picture.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the limit of 1MB");
        }

        if (player.getAvatar() != null) {
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(player.getAvatar())));
        }

        ObjectId fileId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        player.setAvatar(fileId.toString());
        playerRepository.save(player);
        return profilePlayerMapper.toDTO(player);
    }

    @Override
    public byte[] getAvatar(String playerId) throws EntityNotFoundException, IOException {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

        if (player.getAvatar() == null) {
            throw new EntityNotFoundException("Profile picture not found");
        }

        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(player.getAvatar())));
        if (file == null) {
            throw new EntityNotFoundException("Profile picture file not found");
        }

        try (InputStream inputStream = gridFsOperations.getResource(file).getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    @Override
    public void addRegion(String regionId) throws EntityNotFoundException, UnauthorizedException {
        Player player = securityUtils.getAuthenticatedPlayer();
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + regionId));
        player.setRegion(region);
        playerRepository.save(player);
    }
}
