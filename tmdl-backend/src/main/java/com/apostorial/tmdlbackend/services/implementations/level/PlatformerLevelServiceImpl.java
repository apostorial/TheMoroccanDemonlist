package com.apostorial.tmdlbackend.services.implementations.level;

import com.apostorial.tmdlbackend.dtos.level.*;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.mappers.PlayerLevelMapper;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.level.PlatformerLevelService;
import com.apostorial.tmdlbackend.utilities.LevelUtils;
import com.apostorial.tmdlbackend.utilities.PlayerUtils;
import com.apostorial.tmdlbackend.utilities.RecordUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
public class PlatformerLevelServiceImpl implements PlatformerLevelService {
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;
    private final LevelUtils levelUtils;
    private final RecordUtils recordUtils;
    private final PlayerUtils playerUtils;
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlayerLevelMapper playerLevelMapper;

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
        List<PlatformerLevel> levels = platformerLevelRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(PlatformerLevel::getRanking))
                .collect(Collectors.toList());
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
                    : levels.subList(150, levels.size());
            default -> levels;
        };
    }

    @Override
    public List<PlayerLevelRequest> findByRecordHolder(String playerId) {
        return platformerLevelRepository.findByRecordHolderId(playerId).stream()
                .map(playerLevelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PlayerLevelRequest> findHardestLevel(String playerId) throws EntityNotFoundException {
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(playerId);
        if (records.isEmpty()) {
            throw new EntityNotFoundException("Platformer record with ID " + playerId + " not found");
        }
        return records.stream()
                .map(PlatformerRecord::getLevel)
                .min(Comparator.comparingInt(PlatformerLevel::getRanking))
                .map(playerLevelMapper::toDto);
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
    public List<PlayerLevelRequest> findAllByPlayerId(String playerId) {
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(playerId);

        return records.stream()
                .map(PlatformerRecord::getLevel)
                .map(playerLevelMapper::toDto)
                .toList();
    }

    @Override
    public void reorderLevels(List<OrderLevelRequest> orderRequest) {
        List<PlatformerLevel> levels = platformerLevelRepository.findAllById(
                orderRequest.stream().map(OrderLevelRequest::getId).collect(Collectors.toList())
        );

        Map<String, Integer> rankingMap = orderRequest.stream()
                .collect(Collectors.toMap(OrderLevelRequest::getId, OrderLevelRequest::getRanking));

        for (PlatformerLevel level : levels) {
            int newRanking = rankingMap.get(level.getId());
            level.setRanking(newRanking);
            levelUtils.calculatePlatformerPoints(level);
            platformerLevelRepository.save(level);

            List<PlatformerRecord> records = platformerRecordRepository.findAllByLevelId(level.getId());
            for (PlatformerRecord record : records) {
                Player player = playerRepository.findById(record.getPlayer().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Player not found: " + record.getPlayer().getId()));
                recordUtils.updatePlayerPlatformerPoints(record);
                playerUtils.updateRegionPlatformerPoints(player);
            }
        }
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
        if (request.getRecordHolder() != null) {
            Player firstVictor = playerRepository.findById(request.getRecordHolder())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getRecordHolder() + " not found"));
            level.setRecordHolder(firstVictor);
        }
        levelUtils.calculatePlatformerPoints(level);
        return platformerLevelRepository.save(level);
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException {
        PlatformerLevel level = platformerLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found"));

        List<PlatformerRecord> records = platformerRecordRepository.findAllByLevelId(level.getId());
        platformerRecordRepository.deleteByLevelId(level.getId());
        platformerLevelRepository.delete(level);
        for (PlatformerRecord record : records) {
            Player player = record.getPlayer();
            recordUtils.updatePlayerPlatformerPoints(record);
            playerUtils.updateRegionPlatformerPoints(player);
        }
    }
}
