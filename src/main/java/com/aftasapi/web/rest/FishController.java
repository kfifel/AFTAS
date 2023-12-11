package com.aftasapi.web.rest;

import com.aftasapi.dto.FishDto;
import com.aftasapi.dto.FishInputDto;
import com.aftasapi.entity.Fish;
import com.aftasapi.service.FishService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fishes")
@RequiredArgsConstructor
public class FishController {

    private final FishService fishService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<FishDto>> getAllFishes(
            @ParameterObject Pageable pageable
    ) {
        Page<Fish> fishPage = fishService.findAll(pageable);
        return ResponseEntity.ok(
                fishPage.map(fish -> modelMapper.map(fish, FishDto.class))
        );
    }

    @PostMapping
    public ResponseEntity<FishDto> createFish(@RequestBody @Validated FishInputDto fishDto) {
        Fish fish = modelMapper.map(fishDto, Fish.class);
        return ResponseEntity.ok(
                modelMapper.map(fishService.save(fish), FishDto.class));
    }
}
