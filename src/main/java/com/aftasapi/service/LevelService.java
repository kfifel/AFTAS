package com.aftasapi.service;

import com.aftasapi.entity.Level;

import java.util.List;

public interface LevelService {

    Level save(Level level);
    List<Level> findAll();
    Level findByCode(Long code);
    void delete(Long code);
}
