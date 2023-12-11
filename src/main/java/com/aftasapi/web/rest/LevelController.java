package com.aftasapi.web.rest;


import com.aftasapi.dto.LevelDto;
import com.aftasapi.entity.Level;
import com.aftasapi.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/levels")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<LevelDto>> getAllLevels(){
        return ResponseEntity.ok()
                .body(levelService.findAll()
                        .stream()
                        .map(element -> modelMapper.map(element, LevelDto.class))
                        .toList());
    }

    @PostMapping
    public ResponseEntity<LevelDto> save(@RequestBody LevelDto level){
        Level levelSaved = levelService.save(modelMapper.map(level, Level.class));
        return ResponseEntity.ok()
                .body(modelMapper.map(levelSaved, LevelDto.class));
    }
}
