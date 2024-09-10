package com.apostorial.tmdlbackend.services.implementations.record;

import com.apostorial.tmdlbackend.dtos.record.CreatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.exceptions.DuplicateRecordException;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import com.apostorial.tmdlbackend.utilities.PlayerUtils;
import com.apostorial.tmdlbackend.utilities.RecordUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class PlatformerRecordServiceImpl implements PlatformerRecordService {
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;
    private final RecordUtils recordUtils;
    private final PlayerUtils playerUtils;

    @Override
    public PlatformerRecord create(CreatePlatformerRecordRequest request) throws EntityNotFoundException, DuplicateRecordException {
        if (platformerRecordRepository.existsByPlayerIdAndLevelId(request.getPlayer(), request.getLevel())) {
            throw new DuplicateRecordException("A record for this player and level already exists");
        }
        PlatformerRecord record = new PlatformerRecord();
        if (request.getPlayer() != null) {
            Player player = playerRepository.findById(request.getPlayer())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayer() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevel() != null) {
            PlatformerLevel level = platformerLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevel() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordTime(request.getRecordTime());
        platformerRecordRepository.save(record);
        PlatformerRecord savedRecord = platformerRecordRepository.save(record);
        recordUtils.setRecordHolder(savedRecord);
        recordUtils.updatePlayerPlatformerPoints(savedRecord);
        playerUtils.updateRegionPlatformerPoints(savedRecord.getPlayer());
        return savedRecord;
    }

    @Override
    public PlatformerRecord findById(String recordId) throws EntityNotFoundException {
        return platformerRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer record with id " + recordId + " not found"));
    }

    @Override
    public List<PlatformerRecord> findAllByPlayerId(String playerId) {
        return platformerRecordRepository.findAllByPlayerId(playerId);
    }

    @Override
    public List<PlatformerRecord> findAllByLevelId(String levelId) {
        return platformerRecordRepository.findAllByLevelId(levelId);
    }

    @Override
    public List<PlatformerRecord> findAll() {
        return platformerRecordRepository.findAll();
    }

    @Override
    public PlatformerRecord update(String recordId, UpdatePlatformerRecordRequest request) throws EntityNotFoundException {
        PlatformerRecord record = platformerRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer record with id " + recordId + " not found"));
        if (request.getPlayer() != null) {
            Player player = playerRepository.findByUsername(request.getPlayer())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayer() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevel() != null) {
            PlatformerLevel level = platformerLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + request.getLevel() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordTime(request.getRecordTime());
        PlatformerRecord savedRecord = platformerRecordRepository.save(record);
        recordUtils.setRecordHolder(savedRecord);
        recordUtils.updatePlayerPlatformerPoints(savedRecord);
        playerUtils.updateRegionPlatformerPoints(savedRecord.getPlayer());
        return savedRecord;
    }

    @Override
    public void deleteById(String recordId) throws EntityNotFoundException {
        PlatformerRecord record = platformerRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer record with id " + recordId + " not found"));

        Player player = record.getPlayer();
        platformerRecordRepository.delete(record);
        recordUtils.updatePlayerPlatformerPoints(record);
        playerUtils.updateRegionPlatformerPoints(player);
    }
}
