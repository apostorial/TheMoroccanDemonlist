package com.apostorial.tmdlbackend.services.implementations.level;

import com.apostorial.tmdlbackend.dtos.level.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.services.interfaces.level.PlatformerLevelService;
import com.apostorial.tmdlbackend.utilities.LevelUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class PlatformerLevelServiceImpl implements PlatformerLevelService {
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;
    private final LevelUtils levelUtils;

    @Override
    public PlatformerLevel create(CreatePlatformerLevelRequest request) {
        PlatformerLevel level = new PlatformerLevel();
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setRanking(request.getRanking());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        levelUtils.calculatePlatformerPoints(level);
        return platformerLevelRepository.save(level);
    }

    @Override
    public PlatformerLevel findByLevelId(String levelId) throws EntityNotFoundException {
        return platformerLevelRepository.findByLevelId(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));
    }

    @Override
    public List<PlatformerLevel> findByDifficulty(Difficulty difficulty) {
        return platformerLevelRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<PlatformerLevel> findAll(String type) {
        List<PlatformerLevel> levels = platformerLevelRepository.findAll();
        if (type == null) {
            return levels;
        }
        return switch (type) {
            case "main" -> levels.stream()
                    .limit(75)
                    .collect(Collectors.toList());
            case "extended" -> levels.stream()
                    .skip(75)
                    .limit(75)
                    .collect(Collectors.toList());
            case "legacy" -> levels.size() <= 151
                    ? Collections.emptyList()
                    : levels.subList(151, levels.size());
            default -> levels;
        };
    }

    @Override
    public PlatformerLevel update(String levelId, UpdatePlatformerLevelRequest request) throws EntityNotFoundException{
        PlatformerLevel level = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setRanking(request.getRanking());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        if (request.getRecordHolderId() != null) {
            Player firstVictor = playerRepository.findById(request.getRecordHolderId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getRecordHolderId() + " not found"));
            level.setRecordHolder(firstVictor);
        }
        levelUtils.calculatePlatformerPoints(level);
        return platformerLevelRepository.save(level);
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException {
        PlatformerLevel level = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));

        platformerLevelRepository.delete(level);
    }
}
