package com.aftasapi.service.impl;

import com.aftasapi.entity.Fish;
import com.aftasapi.repository.FishRepository;
import com.aftasapi.service.FishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FishServiceImpl implements FishService {

    private final FishRepository fishRepository;

    @Override
    public Page<Fish> findAll(Pageable pageable) {
        return fishRepository.findAll(pageable);
    }

    @Override
    public Fish save(Fish fish) {
        canFishBeSaved(fish);
        return fishRepository.save(fish);
    }

    private void canFishBeSaved(Fish fish) {
        if(fishRepository.findByName(fish.getName()).isPresent()) {
            throw new IllegalArgumentException("Fish with name " + fish.getName() + " already exists");
        }
    }

    @Override
    public void update(Fish fish) {
        if (fishRepository.existsById(fish.getName()))
            fishRepository.delete(fish);

        fishRepository.delete(fish);
    }

    @Override
    public void delete(Fish fish) {
        if (fishRepository.existsById(fish.getName()))
            fishRepository.delete(fish);

        fishRepository.delete(fish);
    }

    @Override
    public Optional<Fish> findByName(String name) {
        return fishRepository.findByName(name);
    }
}
