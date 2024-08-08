package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.PlatformerLevel;
import com.apostorial.tmdlbackend.entities.PlatformerRecord;
import com.apostorial.tmdlbackend.repositories.PlatformerLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public class PlatformerRecordEventListener extends AbstractMongoEventListener<PlatformerRecord> {
    private final PlatformerLevelRepository platformerLevelRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<PlatformerRecord> event) {
        PlatformerRecord platformerRecord = event.getSource();
        setRecordHolder(platformerRecord);
    }

    private void setRecordHolder(PlatformerRecord platformerRecord) {
        PlatformerLevel level = platformerRecord.getLevel();
        if (level != null && (level.getRecordHolder() == null)) {
            level.setRecordHolder(platformerRecord.getPlayer());
            platformerLevelRepository.save(level);
        }
    }
}
