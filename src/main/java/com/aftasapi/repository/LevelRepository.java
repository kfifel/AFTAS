package com.aftasapi.repository;

import com.aftasapi.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByCode(Long code);

    Level findFirstByPointGreaterThanEqual(Integer point);
}