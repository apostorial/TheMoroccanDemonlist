package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClassicLevelEventListener extends AbstractMongoEventListener<ClassicLevel> {

    @Override @Transactional
    public void onAfterConvert(AfterConvertEvent<ClassicLevel> event) {
        ClassicLevel classicLevel = event.getSource();
        calculatePoints(classicLevel);
    }

    private void calculatePoints(ClassicLevel level) {
        if (level.getRanking() == 0 || level.getRanking() > 150) {
            level.setPoints(0.0f);
            level.setMinPoints(0.0f);
        } else {
            float points = (float) (500 * (1 - Math.log(level.getRanking()) / Math.log(151)));
            level.setPoints(Math.round(points * 100) / 100f);
            level.setMinPoints(Math.round(points / 3 * 100) / 100f);
        }
    }
}