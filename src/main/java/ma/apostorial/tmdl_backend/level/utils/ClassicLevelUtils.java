package ma.apostorial.tmdl_backend.level.utils;

import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;

public class ClassicLevelUtils {
    public void calculatePoints(ClassicLevel level) {
        if (level.getRanking() == 0 || level.getRanking() > 150) {
            level.setPoints(0.0);
            level.setMinimumPoints(0.0);
        } else {
            float points = (float) (500 * (1 - Math.log(level.getRanking()) / Math.log(151)));
            level.setPoints(Math.round(points * 100) / 100.0);
            level.setMinimumPoints(Math.round(points / 3 * 100) / 100.0);
        }
    }
}
