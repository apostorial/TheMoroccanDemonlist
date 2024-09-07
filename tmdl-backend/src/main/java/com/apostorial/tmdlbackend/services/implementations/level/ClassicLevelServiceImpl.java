package com.apostorial.tmdlbackend.services.implementations.level;

import com.apostorial.tmdlbackend.dtos.level.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.services.interfaces.level.ClassicLevelService;
import com.apostorial.tmdlbackend.utilities.LevelUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service @AllArgsConstructor @Slf4j
public class ClassicLevelServiceImpl implements ClassicLevelService {
    private final ClassicLevelRepository classicLevelRepository;
    private final PlayerRepository playerRepository;
    private final LevelUtils levelUtils;

    @Override
    public ClassicLevel create(CreateClassicLevelRequest request) {
        ClassicLevel level = new ClassicLevel();
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setRanking(request.getRanking());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        level.setDuration(request.getDuration());
        level.setMinimumCompletion(request.getMinimumCompletion());
        levelUtils.calculateClassicPoints(level);
        return classicLevelRepository.save(level);
    }

    @Override
    public ClassicLevel findByLevelId(String levelId) throws EntityNotFoundException {
        return classicLevelRepository.findByLevelId(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));
    }

    @Override
    public List<ClassicLevel> findByDuration(Duration duration) {
        return classicLevelRepository.findByDuration(duration);
    }

    @Override
    public List<ClassicLevel> findByDifficulty(Difficulty difficulty) {
        return classicLevelRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<ClassicLevel> findAll(String type) {
        List<ClassicLevel> levels = classicLevelRepository.findAll();
        if (type == null) {
            return levels;
        }
        return switch (type) {
            case "main" -> levels.stream()
                    .limit(75)
                    .collect(Collectors.toList());
            case "extended" -> levels.stream()
                    .skip(75)
                    .limit(75)
                    .collect(Collectors.toList());
            case "legacy" -> levels.size() <= 151
                    ? Collections.emptyList()
                    : levels.subList(151, levels.size());
            default -> levels;
        };
    }

    @Override
    public ClassicLevel update(String levelId, UpdateClassicLevelRequest request) throws EntityNotFoundException{
        ClassicLevel level = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setRanking(request.getRanking());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        level.setDuration(request.getDuration());
        level.setMinimumCompletion(request.getMinimumCompletion());
        if (request.getFirstVictorId() != null) {
            Player firstVictor = playerRepository.findById(request.getFirstVictorId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getFirstVictorId() + " not found"));
            level.setFirstVictor(firstVictor);
        }
        levelUtils.calculateClassicPoints(level);
        return classicLevelRepository.save(level);
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException{
        ClassicLevel level = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));

        classicLevelRepository.delete(level);
    }
}
