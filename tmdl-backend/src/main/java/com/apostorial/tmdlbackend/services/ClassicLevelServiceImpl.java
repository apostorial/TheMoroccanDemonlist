package com.apostorial.tmdlbackend.services;

import com.apostorial.tmdlbackend.entities.ClassicLevel;
import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.enums.Duration;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;
import com.apostorial.tmdlbackend.repositories.ClassicLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service @AllArgsConstructor
public class ClassicLevelServiceImpl implements ClassicLevelService {
    private final ClassicLevelRepository classicLevelRepository;

    @Override
    public ClassicLevel create(String levelId, String name, String publisher, Difficulty difficulty, URL link, URL thumbnail, Duration duration, int minimumCompletion) {
        ClassicLevel level = new ClassicLevel();
        level.setLevelId(levelId);
        level.setName(name);
        level.setPublisher(publisher);
        level.setDifficulty(difficulty);
        level.setLink(link);
        level.setThumbnail(thumbnail);
        level.setDuration(duration);
        level.setMinimumCompletion(minimumCompletion);
        return classicLevelRepository.save(level);
    }

    @Override
    public List<ClassicLevel> findByDuration(String duration) {
        return List.of();
    }

    @Override
    public ClassicLevel findById(String levelId) throws EntityNotFoundException {
        return classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));
    }

    @Override
    public List<ClassicLevel> findAll() {
        return classicLevelRepository.findAll();
    }

    @Override
    public void deleteById(String levelId) throws EntityNotFoundException{
        ClassicLevel classicLevel = classicLevelRepository.findById(levelId)
                .orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found"));

        classicLevelRepository.delete(classicLevel);
    }

    @Override
    public List<ClassicLevel> findByDifficulty(Difficulty difficulty) {
        return List.of();
    }
}
