package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.level.PlatformerLevel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlatformerLevelEventListener extends AbstractMongoEventListener<PlatformerLevel> {

    @Override @Transactional
    public void onAfterConvert(AfterConvertEvent<PlatformerLevel> event) {
        PlatformerLevel platformerLevel = event.getSource();
        calculatePoints(platformerLevel);
    }

    private void calculatePoints(PlatformerLevel level) {
        float calculatedPoints = (float) (500 * (1 - Math.log(level.getRanking()) / Math.log(151)));
        level.setPoints(Math.round(calculatedPoints * 100) / 100f);
    }
}