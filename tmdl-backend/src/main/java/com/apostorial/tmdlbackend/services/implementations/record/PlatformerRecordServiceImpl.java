package com.apostorial.tmdlbackend.services.implementations.record;

import com.apostorial.tmdlbackend.dtos.record.CreatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdatePlatformerRecordRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class PlatformerRecordServiceImpl implements PlatformerRecordService {
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlayerRepository playerRepository;

    @Override
    public PlatformerRecord create(CreatePlatformerRecordRequest request) throws EntityNotFoundException {
        PlatformerRecord record = new PlatformerRecord();
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayerId() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevelId() != null) {
            PlatformerLevel level = platformerLevelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevelId() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordTime(request.getRecordTime());
        return platformerRecordRepository.save(record);
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
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayerId() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevelId() != null) {
            PlatformerLevel level = platformerLevelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + request.getLevelId() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordTime(request.getRecordTime());
        return platformerRecordRepository.save(record);
    }

    @Override
    public void deleteById(String recordId) throws EntityNotFoundException {
        PlatformerRecord record = platformerRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Platformer record with id " + recordId + " not found"));

        platformerRecordRepository.delete(record);
    }
}
