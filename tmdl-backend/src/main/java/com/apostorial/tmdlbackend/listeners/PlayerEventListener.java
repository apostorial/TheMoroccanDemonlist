package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.RegionRepository;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import com.apostorial.tmdlbackend.services.interfaces.record.ClassicRecordService;
import com.apostorial.tmdlbackend.services.interfaces.record.PlatformerRecordService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component @AllArgsConstructor
public class PlayerEventListener extends AbstractMongoEventListener<Player> {
    private final PlayerService playerService;
    private final RegionRepository regionRepository;
    private final PlayerRepository playerRepository;
    private final ClassicRecordService classicRecordService;
    private final PlatformerRecordService platformerRecordService;

    @Override @Transactional
    public void onBeforeConvert(BeforeConvertEvent<Player> event) {
        Player player = event.getSource();
        updateRegionClassicPoints(player);
        updateRegionPlatformerPoints(player);
        updatePlayerClassicPoints(player);
        updatePlayerPlatformerPoints(player);
    }

    private void updatePlayerClassicPoints(Player player) {
        float totalClassicPoints = 0;
        List<ClassicRecord> records = classicRecordService.findAllByPlayerId(player.getId());
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

    private void updatePlayerPlatformerPoints(Player player) {
        float totalPlatformerPoints = 0;
        List<PlatformerRecord> records = platformerRecordService.findAllByPlayerId(player.getId());
        for (PlatformerRecord record : records) {
            PlatformerLevel level = record.getLevel();
            totalPlatformerPoints += level.getPoints();
        }
        if (player.getPlatformerPoints() != totalPlatformerPoints) {
            player.setPlatformerPoints(totalPlatformerPoints);
            playerRepository.save(player);
        }
    }

    private void updateRegionClassicPoints(Player player) {
        if (player.getRegion() == null) {
            return;
        }
        float totalClassicPoints = 0;
        Region region = player.getRegion();
        List<Player> players = playerService.findAllByRegionId(region.getId());
        for (Player regionPlayer : players) {
            totalClassicPoints += regionPlayer.getClassicPoints();
        }
        if (region.getClassicPoints() != totalClassicPoints) {
            region.setClassicPoints(totalClassicPoints);
            regionRepository.save(region);
        }
    }

    private void updateRegionPlatformerPoints(Player player) {
        if (player.getRegion() == null) {
            return;
        }
        float totalPlatformerPoints = 0;
        Region region = player.getRegion();
        List<Player> players = playerService.findAllByRegionId(region.getId());
        for (Player regionPlayer : players) {
            totalPlatformerPoints += regionPlayer.getPlatformerPoints();
        }
        if (region.getPlatformerPoints() != totalPlatformerPoints) {
            region.setPlatformerPoints(totalPlatformerPoints);
            regionRepository.save(region);
        }
    }
}
