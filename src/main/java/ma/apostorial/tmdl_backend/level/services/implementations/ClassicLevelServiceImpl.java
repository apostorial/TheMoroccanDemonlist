package ma.apostorial.tmdl_backend.level.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelCreationDTO;
import ma.apostorial.tmdl_backend.level.dtos.ClassicLevelUpdateDTO;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.level.mappers.ClassicLevelMapper;
import ma.apostorial.tmdl_backend.level.repositories.ClassicLevelRepository;
import ma.apostorial.tmdl_backend.level.services.interfaces.ClassicLevelService;
import ma.apostorial.tmdl_backend.level.utils.ClassicLevelUtils;

@Service @Transactional @RequiredArgsConstructor
public class ClassicLevelServiceImpl implements ClassicLevelService {
    private final ClassicLevelRepository classicLevelRepository;
    private final ClassicLevelMapper classicLevelMapper;
    private final ClassicLevelUtils classicLevelUtils;

    @Override
    public ClassicLevel create(ClassicLevelCreationDTO request) {
        ClassicLevel level = classicLevelMapper.fromClassicLevelCreationDTOToEntity(request);
        classicLevelUtils.calculatePoints(level);
        return classicLevelRepository.save(level);
    }

    @Override
    public ClassicLevel findById(UUID id) {
        return classicLevelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Classic level with id " + id + " not found."));
    }

    @Override
    public ClassicLevel update(UUID id, ClassicLevelUpdateDTO request) {
        ClassicLevel level = classicLevelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Classic level with id " + id + " not found."));
        classicLevelMapper.updateFromDTO(request, level);
        return classicLevelRepository.save(level);
    }

    @Override
    public void deleteById(UUID id) {
        classicLevelRepository.deleteById(id);
    }

    @Override
    public List<ClassicLevel> queryLevels(String query, Difficulty difficulty, Duration duration, String type) {
        return classicLevelRepository.queryLevels(query, difficulty, duration, type);
    }
}
