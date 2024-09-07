package com.apostorial.tmdlbackend.services.implementations.record;

import com.apostorial.tmdlbackend.dtos.record.CreateClassicRecordRequest;
import com.apostorial.tmdlbackend.dtos.record.UpdateClassicRecordRequest;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.exceptions.DuplicateRecordException;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.ClassicRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.record.ClassicRecordService;
import com.apostorial.tmdlbackend.utilities.PlayerUtils;
import com.apostorial.tmdlbackend.utilities.RecordUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor @Slf4j
public class ClassicRecordServiceImpl implements ClassicRecordService {
    private final ClassicRecordRepository classicRecordRepository;
    private final ClassicLevelRepository classicLevelRepository;
    private final PlayerRepository playerRepository;
    private final RecordUtils recordUtils;
    private final PlayerUtils playerUtils;

    @Override
    public ClassicRecord create(CreateClassicRecordRequest request) throws EntityNotFoundException, DuplicateRecordException {
        if (classicRecordRepository.existsByPlayerIdAndLevelId(request.getPlayerId(), request.getLevelId())) {
            throw new DuplicateRecordException("A record for this player and level already exists");
        }
        ClassicRecord record = new ClassicRecord();
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayerId() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevelId() != null) {
            ClassicLevel level = classicLevelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevelId() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordPercentage(request.getRecordPercentage());
        ClassicRecord savedRecord = classicRecordRepository.save(record);
        recordUtils.setFirstVictor(savedRecord);
        recordUtils.updatePlayerClassicPoints(savedRecord);
        playerUtils.updateRegionClassicPoints(savedRecord.getPlayer());
        return savedRecord;
    }

    @Override
    public ClassicRecord findById(String recordId) throws EntityNotFoundException {
        return classicRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Classic record with id " + recordId + " not found"));
    }

    @Override
    public List<ClassicRecord> findAllByPlayerId(String playerId) {
        return classicRecordRepository.findAllByPlayerId(playerId);
    }

    @Override
    public List<ClassicRecord> findAllByLevelId(String levelId) {
        return classicRecordRepository.findAllByLevelId(levelId);
    }

    @Override
    public List<ClassicRecord> findAll() {
        return classicRecordRepository.findAll();
    }

    @Override
    public ClassicRecord update(String recordId, UpdateClassicRecordRequest request) throws EntityNotFoundException {
        ClassicRecord record = classicRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Classic record with id " + recordId + " not found"));
        if (request.getPlayerId() != null) {
            Player player = playerRepository.findById(request.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayerId() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevelId() != null) {
            ClassicLevel level = classicLevelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevelId() + " not found"));
            record.setLevel(level);
        }
        record.setLink(request.getLink());
        record.setRecordPercentage(request.getRecordPercentage());
        ClassicRecord savedRecord = classicRecordRepository.save(record);
        recordUtils.setFirstVictor(savedRecord);
        recordUtils.updatePlayerClassicPoints(savedRecord);
        playerUtils.updateRegionClassicPoints(savedRecord.getPlayer());
        return savedRecord;
    }

    @Override
    public void deleteById(String recordId) throws EntityNotFoundException {
        ClassicRecord record = classicRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Classic record with id " + recordId + " not found"));

        classicRecordRepository.delete(record);
    }
}
