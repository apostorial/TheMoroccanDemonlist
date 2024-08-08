package com.apostorial.tmdlbackend.listeners;

import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.record.ClassicRecord;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component @AllArgsConstructor
public class ClassicRecordEventListener extends AbstractMongoEventListener<ClassicRecord> {
    private final ClassicLevelRepository classicLevelRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<ClassicRecord> event) {
        ClassicRecord classicRecord = event.getSource();
        setFirstVictor(classicRecord);
    }

    private void setFirstVictor(ClassicRecord classicRecord) {
        ClassicLevel level = classicRecord.getLevel();
        if (level != null && (level.getFirstVictor() == null || classicRecord.getRecordPercentage() == 100)) {
            level.setFirstVictor(classicRecord.getPlayer());
            classicLevelRepository.save(level);
        }
    }
}
