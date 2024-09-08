package com.apostorial.tmdlbackend.services.implementations.level;

import com.apostorial.tmdlbackend.dtos.level.CreatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.LevelCountRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdatePlatformerLevelRequest;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.level.PlatformerLevelService;
import com.apostorial.tmdlbackend.utilities.LevelUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class PlatformerLevelServiceImpl implements PlatformerLevelService {
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;
    private final LevelUtils levelUtils;
    private final PlatformerRecordRepository platformerRecordRepository;

    @Override
    public PlatformerLevel create(CreatePlatformerLevelRequest request) {
        PlatformerLevel level = new PlatformerLevel();
        level.setId(request.getId());
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
    public PlatformerLevel findById(String levelId) throws EntityNotFoundException {
        return platformerLevelRepository.findById(levelId)
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
    public List<PlatformerLevel> findByRecordHolder(String playerId) {
        return platformerLevelRepository.findByRecordHolder(playerId);
    }

    @Override
    public PlatformerLevel findHardestLevel(String playerId) throws EntityNotFoundException {
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(playerId);
        return records.stream()
                .map(PlatformerRecord::getLevel)
                .min(Comparator.comparingInt(PlatformerLevel::getRanking))
                .orElseThrow(() -> new EntityNotFoundException("No level found for player with ID: " + playerId));
    }

    @Override
    public LevelCountRequest getLevelCount(String playerId) {
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(playerId);

        List<PlatformerLevel> levels = records.stream()
                .map(PlatformerRecord::getLevel)
                .filter(Objects::nonNull)
                .toList();

        long main = levels.stream()
                .filter(level -> level.getRanking() <= 75)
                .count();
        long extended = levels.stream()
                .filter(level -> level.getRanking() > 75 && level.getRanking() <= 150)
                .count();
        long legacy = levels.stream()
                .filter(level -> level.getRanking() > 150)
                .count();
        return new LevelCountRequest(main, extended, legacy);
    }

    @Override
    public List<PlatformerLevel> findAllByPlayerId(String playerId) {
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(playerId);

        return records.stream()
                .map(PlatformerRecord::getLevel)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public PlatformerLevel update(String levelId, UpdatePlatformerLevelRequest request) throws EntityNotFoundException{
        PlatformerLevel level = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));
        level.setId(request.getId());
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
