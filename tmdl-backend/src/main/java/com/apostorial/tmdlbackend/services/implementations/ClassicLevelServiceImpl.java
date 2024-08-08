package com.apostorial.tmdlbackend.services.implementations;

import com.apostorial.tmdlbackend.dtos.level.CreateClassicLevelRequest;
import com.apostorial.tmdlbackend.dtos.level.UpdateClassicLevelRequest;
import com.apostorial.tmdlbackend.entities.level.ClassicLevel;
import com.apostorial.tmdlbackend.entities.Player;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.level.ClassicLevelRepository;
import com.apostorial.tmdlbackend.repositories.PlayerRepository;
import com.apostorial.tmdlbackend.services.interfaces.ClassicLevelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class ClassicLevelServiceImpl implements ClassicLevelService {
    private final ClassicLevelRepository classicLevelRepository;
    private final PlayerRepository playerRepository;

    @Override
    public ClassicLevel create(CreateClassicLevelRequest request) {
        ClassicLevel level = new ClassicLevel();
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        level.setDuration(request.getDuration());
        level.setMinimumCompletion(request.getMinimumCompletion());
        return classicLevelRepository.save(level);
    }

    @Override
    public ClassicLevel findById(String levelId) throws EntityNotFoundException {
        return classicLevelRepository.findById(levelId)
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
    public List<ClassicLevel> findAll() {
        return classicLevelRepository.findAll();
    }

    @Override
    public ClassicLevel update(String levelId, UpdateClassicLevelRequest request) throws EntityNotFoundException{
        ClassicLevel level = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));
        level.setLevelId(request.getLevelId());
        level.setName(request.getName());
        level.setPublisher(request.getPublisher());
        level.setDifficulty(request.getDifficulty());
        level.setLink(request.getLink());
        level.setThumbnail(request.getThumbnail());
        level.setDuration(request.getDuration());
        level.setMinimumCompletion(request.getMinimumCompletion());
        if (request.getFirstVictorId() != null) {
            Player firstVictor = playerRepository.findById(request.getFirstVictorId())
                    .orElseThrow(() -> new EntityNotFoundException("Player with id " + request.getFirstVictorId() + " not found"));
            level.setFirstVictor(firstVictor);
        }
        return classicLevelRepository.save(level);
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException{
        ClassicLevel classicLevel = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));

        classicLevelRepository.delete(classicLevel);
    }
}
