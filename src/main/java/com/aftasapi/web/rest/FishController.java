package com.aftasapi.web.rest;

import com.aftasapi.web.dto.FishDto;
import com.aftasapi.web.dto.FishInputDto;
import com.aftasapi.entity.Fish;
import com.aftasapi.service.FishService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
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
    public ResponseEntity<List<FishDto>> getAllFishes(
            @ParameterObject Pageable pageable
    ) {
        List<Fish> fishPage = fishService.findAll(pageable).stream().toList();
        return ResponseEntity.ok(
                fishPage.stream().map(fish -> modelMapper.map(fish, FishDto.class)).toList()
        );
    }

    @PostMapping
    public ResponseEntity<FishDto> createFish(@RequestBody @Validated FishInputDto fishDto) {
        Fish fish = modelMapper.map(fishDto, Fish.class);
        return ResponseEntity.ok(
                modelMapper.map(fishService.save(fish), FishDto.class));
    }
}
