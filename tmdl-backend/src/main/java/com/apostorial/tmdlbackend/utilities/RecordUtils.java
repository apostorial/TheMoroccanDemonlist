package com.apostorial.tmdlbackend.utilities;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.ClassicRecordRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor
public class RecordUtils {
    private final ClassicLevelRepository classicLevelRepository;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final ClassicRecordRepository classicRecordRepository;
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlayerRepository playerRepository;

    public void setFirstVictor(ClassicRecord record) {
        ClassicLevel level = record.getLevel();
        if (level.getFirstVictor() == null && record.getRecordPercentage() == 100) {
            level.setFirstVictor(record.getPlayer());
            classicLevelRepository.save(level);
        }
    }

    public void updatePlayerClassicPoints(ClassicRecord classicRecord) {
        float totalClassicPoints = 0;
        Player player = classicRecord.getPlayer();
        List<ClassicRecord> records = classicRecordRepository.findAllByPlayerId(player.getId());
        for (ClassicRecord record : records) {
            ClassicLevel level = record.getLevel();
            if (record.getRecordPercentage() == 100) {
                totalClassicPoints += level.getPoints();
            } else {
                totalClassicPoints += level.getMinPoints();
            }
        }
        if (player.getClassicPoints() != totalClassicPoints) {
            player.setClassicPoints(totalClassicPoints);
            playerRepository.save(player);
        }
    }

    public void setRecordHolder(PlatformerRecord record) {
        PlatformerLevel level = record.getLevel();
        if (level != null && (level.getRecordHolder() == null)) {
            level.setRecordHolder(record.getPlayer());
            platformerLevelRepository.save(level);
        }
    }

    public void updatePlayerPlatformerPoints(PlatformerRecord platformerRecord) {
        float totalPlatformerPoints = 0;
        Player player = platformerRecord.getPlayer();
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(player.getId());
        for (PlatformerRecord record : records) {
            PlatformerLevel level = record.getLevel();
            totalPlatformerPoints += level.getPoints();
        }
        if (player.getPlatformerPoints() != totalPlatformerPoints) {
            player.setPlatformerPoints(totalPlatformerPoints);
            playerRepository.save(player);
        }
    }
}
