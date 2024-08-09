package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.ClassicRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component @AllArgsConstructor
public class ClassicRecordEventListener extends AbstractMongoEventListener<ClassicRecord> {
    private final ClassicLevelRepository classicLevelRepository;
    private final ClassicRecordRepository classicRecordRepository;
    private final PlayerRepository playerRepository;

    @Override @Transactional
    public void onBeforeConvert(BeforeConvertEvent<ClassicRecord> event) {
        ClassicRecord classicRecord = event.getSource();
        setFirstVictor(classicRecord);
    }

    @Override @Transactional
    public void onAfterConvert(AfterConvertEvent<ClassicRecord> event) {
        ClassicRecord classicRecord = event.getSource();
        updatePlayerClassicPoints(classicRecord);
    }

    private void setFirstVictor(ClassicRecord record) {
        ClassicLevel level = record.getLevel();
        if (level.getFirstVictor() == null && record.getRecordPercentage() == 100) {
            level.setFirstVictor(record.getPlayer());
            classicLevelRepository.save(level);
        }
    }

    private void updatePlayerClassicPoints(ClassicRecord classicRecord) {
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
}
