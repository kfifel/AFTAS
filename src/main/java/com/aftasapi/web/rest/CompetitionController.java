package com.aftasapi.web.rest;

import com.aftasapi.dto.CompetitionDTO;
import com.aftasapi.entity.Competition;
import com.aftasapi.service.CompetitionService;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;
    private final ModelMapper modelMapper;

    public CompetitionController(
            @Qualifier("competitionServiceImpl2")
            CompetitionService competitionService,
            ModelMapper modelMapper
    ) {
        this.competitionService = competitionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> getAllCompetition(@ParameterObject Pageable pageable) {
        List<Competition> competitions = competitionService.findAll(pageable);
        return ResponseEntity.ok(
                competitions
                    .stream()
                    .map(competition -> modelMapper.map(competition, CompetitionDTO.class))
                    .toList());
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody @Validated CompetitionDTO competitionDTO) {
        Competition save = competitionService.save(modelMapper.map(competitionDTO, Competition.class));
        return ResponseEntity.ok(modelMapper.map(save, CompetitionDTO.class));
    }
}
