package com.aftasapi.service;

import com.aftasapi.entity.Fish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FishService {

    Page<Fish> findAll(Pageable pageable);

    Fish save(Fish fish);

    void update(Fish fish);

    void delete(Fish fish);

    Optional<Fish> findByName(String name);
}
