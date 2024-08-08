package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class ClassicLevelEventListener extends AbstractMongoEventListener<ClassicLevel> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<ClassicLevel> event) {
        ClassicLevel classicLevel = event.getSource();
        calculatePoints(classicLevel);
    }

    private void calculatePoints(ClassicLevel classicLevel) {
        if (classicLevel.getRanking() == 0 || classicLevel.getRanking() > 150) {
            classicLevel.setPoints(0.0f);
            classicLevel.setMinPoints(0.0f);
        } else {
            float points = (float) (500 * (1 - Math.log(classicLevel.getRanking()) / Math.log(151)));
            classicLevel.setPoints(Math.round(points * 100) / 100f);
            classicLevel.setMinPoints(Math.round(points / 3 * 100) / 100f);
        }
    }
}