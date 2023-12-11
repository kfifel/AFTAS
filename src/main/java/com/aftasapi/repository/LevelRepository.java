package com.aftasapi.repository;

import com.aftasapi.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByCode(Long code);

    Optional<Level> findOneByCodeGreaterThan(Long code);

    Optional<Level> findOneByCodeGreaterThanOrPointGreaterThanEqual(Long code, Integer point);

    List<Level> findByCodeGreaterThan(Long code);

    List<Level> findByCodeGreaterThanAndPointGreaterThanEqual(Long code, Integer point);

    Level findFirstByPointGreaterThanEqual(Integer point);
}