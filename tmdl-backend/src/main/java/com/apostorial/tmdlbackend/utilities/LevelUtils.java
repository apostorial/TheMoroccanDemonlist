package com.apostorial.tmdlbackend.utilities;

import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class LevelUtils {

    public void calculateClassicPoints(ClassicLevel level) {
        if (level.getRanking() == 0 || level.getRanking() > 150) {
            level.setPoints(0.0f);
            level.setMinPoints(0.0f);
        } else {
            float points = (float) (500 * (1 - Math.log(level.getRanking()) / Math.log(151)));
            level.setPoints(Math.round(points * 100) / 100f);
            level.setMinPoints(Math.round(points / 3 * 100) / 100f);
        }
    }

    public void calculatePlatformerPoints(PlatformerLevel level) {
        float calculatedPoints = (float) (500 * (1 - Math.log(level.getRanking()) / Math.log(151)));
        level.setPoints(Math.round(calculatedPoints * 100) / 100f);
    }
}
