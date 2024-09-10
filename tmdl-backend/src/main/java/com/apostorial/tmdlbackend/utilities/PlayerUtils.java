package com.apostorial.tmdlbackend.utilities;

import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.entities.Region;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.entities.record.PlatformerRecord;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.repositories.RegionRepository;
import com.apostorial.tmdlbackend.repositories.record.ClassicRecordRepository;
import com.apostorial.tmdlbackend.repositories.record.PlatformerRecordRepository;
import com.apostorial.tmdlbackend.services.interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component @RequiredArgsConstructor
public class PlayerUtils {
    private final PlayerService playerService;
    private final RegionRepository regionRepository;
    private final PlayerRepository playerRepository;
    private final ClassicRecordRepository classicRecordRepository;
    private final PlatformerRecordRepository platformerRecordRepository;

    public void updatePlayerClassicPoints(Player player) {
        Double totalClassicPoints = 0.0;
        List<ClassicRecord> records = classicRecordRepository.findAllByPlayerId(player.getId());
        for (ClassicRecord record : records) {
            ClassicLevel level = record.getLevel();
            if (record.getRecordPercentage() == 100) {
                totalClassicPoints += level.getPoints();
            } else {
                totalClassicPoints += level.getMinPoints();
            }
        }
        if (!Objects.equals(player.getClassicPoints(), totalClassicPoints)) {
            player.setClassicPoints(totalClassicPoints);
            playerRepository.save(player);
        }
    }

    public void updatePlayerPlatformerPoints(Player player) {
        Double totalPlatformerPoints = 0.0;
        List<PlatformerRecord> records = platformerRecordRepository.findAllByPlayerId(player.getId());
        for (PlatformerRecord record : records) {
            PlatformerLevel level = record.getLevel();
            totalPlatformerPoints += level.getPoints();
        }
        if (!Objects.equals(player.getPlatformerPoints(), totalPlatformerPoints)) {
            player.setPlatformerPoints(totalPlatformerPoints);
            playerRepository.save(player);
        }
    }

    public void updateRegionClassicPoints(Player player) {
        if (player.getRegion() == null) {
            return;
        }
        Double totalClassicPoints = 0.0;
        Region region = player.getRegion();
        List<Player> players = playerService.findAllByRegionId(region.getId());
        for (Player regionPlayer : players) {
            totalClassicPoints += regionPlayer.getClassicPoints();
        }
        if (!Objects.equals(region.getClassicPoints(), totalClassicPoints)) {
            region.setClassicPoints(totalClassicPoints);
            regionRepository.save(region);
        }
    }

    public void updateRegionPlatformerPoints(Player player) {
        if (player.getRegion() == null) {
            return;
        }
        Double totalPlatformerPoints = 0.0;
        Region region = player.getRegion();
        List<Player> players = playerService.findAllByRegionId(region.getId());
        for (Player regionPlayer : players) {
            totalPlatformerPoints += regionPlayer.getPlatformerPoints();
        }
        if (!Objects.equals(region.getPlatformerPoints(), totalPlatformerPoints)) {
            region.setPlatformerPoints(totalPlatformerPoints);
            regionRepository.save(region);
        }
    }
}
