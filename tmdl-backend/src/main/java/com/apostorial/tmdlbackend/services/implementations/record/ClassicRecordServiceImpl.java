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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @AllArgsConstructor
public class ClassicRecordServiceImpl implements ClassicRecordService {
    private final ClassicRecordRepository classicRecordRepository;
    private final ClassicLevelRepository classicLevelRepository;
    private final PlayerRepository playerRepository;
    private final RecordUtils recordUtils;
    private final PlayerUtils playerUtils;

    @Override
    public ClassicRecord create(CreateClassicRecordRequest request) throws EntityNotFoundException, DuplicateRecordException {
        if (classicRecordRepository.existsByPlayerIdAndLevelId(request.getPlayer(), request.getLevel())) {
            throw new DuplicateRecordException("A record for this player and level already exists");
        }
        ClassicRecord record = new ClassicRecord();
        if (request.getPlayer() != null) {
            Player player = playerRepository.findById(request.getPlayer())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getPlayer() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevel() != null) {
            ClassicLevel level = classicLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevel() + " not found"));
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
    public List<ClassicRecord> findAllByLevelId(String levelId) throws EntityNotFoundException {
        List<ClassicRecord> records = classicRecordRepository.findAllByLevelId(levelId);

        ClassicRecord firstVictor = getFirstVictorRecord(levelId);

        return Stream.concat(
                        Stream.of(firstVictor),
                        records.stream()
                                .filter(record -> !record.equals(firstVictor))
                                .sorted(Comparator.comparing(ClassicRecord::getRecordPercentage, Comparator.reverseOrder()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public ClassicRecord getFirstVictorRecord(String levelId) throws EntityNotFoundException {
        ClassicLevel level = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Level not found with ID: " + levelId));

        Player firstVictor = level.getFirstVictor();

        return classicRecordRepository.findByLevelIdAndPlayerId(levelId, firstVictor.getId())
                .orElseThrow(() -> new EntityNotFoundException("First victor record not found for level ID: " + levelId + " and player ID: " + firstVictor.getId()));
    }

    @Override
    public Page<ClassicRecord> findAll(Pageable pageable) {
        return classicRecordRepository.findAll(pageable);
    }

    @Override
    public ClassicRecord update(String recordId, UpdateClassicRecordRequest request) throws EntityNotFoundException {
        ClassicRecord record = classicRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Classic record with id " + recordId + " not found"));
        if (request.getPlayer() != null) {
            Player player = playerRepository.findByUsername(request.getPlayer())
                    .orElseThrow(() -> new EntityNotFoundException("Player with username " + request.getPlayer() + " not found"));
            record.setPlayer(player);
        }
        if (request.getLevel() != null) {
            ClassicLevel level = classicLevelRepository.findById(request.getLevel())
                    .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + request.getLevel() + " not found"));
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

        Player player = record.getPlayer();
        classicRecordRepository.delete(record);
        recordUtils.updatePlayerClassicPoints(record);
        playerUtils.updateRegionClassicPoints(player);
    }
}
