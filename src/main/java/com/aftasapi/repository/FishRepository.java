package com.aftasapi.repository;

import com.aftasapi.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishRepository extends JpaRepository<Fish, String> {
    Optional<Fish> findByName(String name);
}