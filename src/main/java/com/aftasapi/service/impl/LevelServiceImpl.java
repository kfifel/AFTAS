package com.aftasapi.service.impl;

import com.aftasapi.entity.Level;
import com.aftasapi.repository.LevelRepository;
import com.aftasapi.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    @Override
    public Level save(Level level) {
        canAddLevel(level);
        return levelRepository.save(level);
    }

    private void canAddLevel(Level level) {
        if (levelRepository.findByCode(level.getCode()).isPresent()) {
            throw new IllegalArgumentException("Level already exists with this code: " + level.getCode());
        }

        Level matchingLevel = levelRepository.findFirstByPointGreaterThanEqual(level.getPoint());
        if ( matchingLevel != null) {
            throw new IllegalArgumentException("A new level cannot be created with a point lower than or equal an existing level");
        }
    }

    @Override
    public List<Level> findAll() {
        return levelRepository.findAll();
    }

    @Override
    public Level findByCode(Long code) {
        return levelRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Level doesn't exist with this code: " + code));
    }

    @Override
    public void delete(Long code) {
        findByCode(code);
        levelRepository.delete(findByCode(code));
    }
}
