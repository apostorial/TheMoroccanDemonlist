package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class PlatformerLevelEventListener extends AbstractMongoEventListener<PlatformerLevel> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<PlatformerLevel> event) {
        PlatformerLevel platformerLevel = event.getSource();
        calculatePoints(platformerLevel);
    }

    private void calculatePoints(PlatformerLevel platformerLevel) {
        float calculatedPoints = (float) (500 * (1 - Math.log(platformerLevel.getRanking()) / Math.log(151)));
        platformerLevel.setPoints(Math.round(calculatedPoints * 100) / 100f);
    }
}