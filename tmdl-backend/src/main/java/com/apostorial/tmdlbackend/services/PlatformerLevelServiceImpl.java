package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.dtos.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class PlatformerLevelServiceImpl implements PlatformerLevelService {
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;

    @Override
    public PlatformerLevel create(CreatePlatformerLevelRequest request) {
        PlatformerLevel level = new PlatformerLevel();
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        return platformerLevelRepository.save(level);
    }

    @Override
    public PlatformerLevel findById(String levelId) throws EntityNotFoundException {
        return platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));
    }

    @Override
    public List<PlatformerLevel> findByDifficulty(Difficulty difficulty) {
        return platformerLevelRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<PlatformerLevel> findAll() {
        return platformerLevelRepository.findAll();
    }

    @Override
    public PlatformerLevel update(String levelId, UpdatePlatformerLevelRequest request) throws EntityNotFoundException{
        PlatformerLevel level = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        if (request.getRecordHolderId() != null) {
            Player firstVictor = playerRepository.findById(request.getRecordHolderId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getRecordHolderId() + " not found"));
            level.setRecordHolder(firstVictor);
        }
        return platformerLevelRepository.save(level);
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException {
        PlatformerLevel platformerLevel = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));

        platformerLevelRepository.delete(platformerLevel);
    }
}
