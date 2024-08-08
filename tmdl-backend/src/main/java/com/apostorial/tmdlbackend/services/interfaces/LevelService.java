package com.apostorial.tmdlbackend.services.interfaces;

import com.apostorial.tmdlbackend.enums.Difficulty;
import com.apostorial.tmdlbackend.exceptions.EntityNotFoundException;

import java.util.List;
public interface LevelService<T> {
    T findById(String levelId) throws EntityNotFoundException;
    List<T> findAll();
    void deleteById(String levelId) throws EntityNotFoundException;
    List<T> findByDifficulty(Difficulty difficulty);
}
