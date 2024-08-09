package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.level.PlatformerLevelRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component @AllArgsConstructor
public class PlatformerRecordEventListener extends AbstractMongoEventListener<PlatformerRecord> {
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlayerRepository playerRepository;

    @Override @Transactional
    public void onBeforeConvert(BeforeConvertEvent<PlatformerRecord> event) {
        PlatformerRecord platformerRecord = event.getSource();
        setRecordHolder(platformerRecord);
    }

    @Override @Transactional
    public void onAfterConvert(AfterConvertEvent<PlatformerRecord> event) {
        PlatformerRecord platformerRecord = event.getSource();
        updatePlayerPlatformerPoints(platformerRecord);
    }

    private void setRecordHolder(PlatformerRecord record) {
        PlatformerLevel level = record.getLevel();
        if (level != null && (level.getRecordHolder() == null)) {
            level.setRecordHolder(record.getPlayer());
            platformerLevelRepository.save(level);
        }
    }

    private void updatePlayerPlatformerPoints(PlatformerRecord platformerRecord) {
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
