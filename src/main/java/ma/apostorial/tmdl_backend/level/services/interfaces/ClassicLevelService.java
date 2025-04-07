package ma.apostorial.tmdl_backend.level.services.interfaces;

import java.util.List;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelCreationDTO;
import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelUpdateDTO;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

public interface ClassicLevelService {
    ClassicLevel create(ClassicLevelCreationDTO request);
    ClassicLevel findById(UUID id);
    ClassicLevel update(UUID id, ClassicLevelUpdateDTO request);
    void deleteById(UUID id);

    List<ClassicLevel> queryLevels(String query, Difficulty difficulty, Duration duration, String type);
}
