package com.aftasapi.web.rest;

import com.aftasapi.common.PaginatedResponse;
import com.aftasapi.web.dto.*;
import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Hunting;
import com.aftasapi.entity.Member;
import com.aftasapi.entity.Ranking;
import com.aftasapi.web.exception.ResourceNotFoundException;
import com.aftasapi.service.CompetitionService;
import com.aftasapi.service.HuntingService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/competitions")
@RequiredArgsConstructor
@OpenAPIDefinition
public class CompetitionController {

    private final CompetitionService competitionService;
    private final ModelMapper modelMapper;
    private final HuntingService huntingService;

    @GetMapping
    @Operation(summary = "Get all competitions")
    public ResponseEntity<PaginatedResponse<CompetitionDTO>> getAllCompetition(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, name = "query") String query
    ) {
        Page<Competition> competitions = competitionService.findAll(pageable, query);
        PaginatedResponse<CompetitionDTO> response = PaginatedResponse.<CompetitionDTO>builder()
                .content(
                            competitions.stream()
                                .map(competition -> modelMapper.map(competition, CompetitionDTO.class))
                                .toList()
                )
                .pageNumber(competitions.getNumber())
                .totalPages(competitions.getTotalPages())
                .totalElements(competitions.getTotalElements())
                .pageSize(competitions.getSize())
                .build();
        return ResponseEntity.ok().body(response) ;
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody @Valid CompetitionDTO competitionDTO) {
        Competition save = competitionService.save(modelMapper.map(competitionDTO, Competition.class));
        return ResponseEntity.ok(modelMapper.map(save, CompetitionDTO.class));
    }

    @GetMapping("/{competitionCode}")
    public ResponseEntity<Competition> getCompetitionByCode(@PathVariable String competitionCode) throws ResourceNotFoundException {
        Competition competition = competitionService.findByCode(competitionCode)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with code " + competitionCode + " not found"));
        return ResponseEntity.ok(competition);
    }

    @PostMapping("/{competitionCode}/registrations")
    public ResponseEntity<Ranking> registerMember(
            @PathVariable String competitionCode,
            @RequestBody @Validated CompetitionRegistrationDTO competitionRegistrationDTO
    ) throws ResourceNotFoundException {
        Ranking save = competitionService.registerMember(competitionCode, competitionRegistrationDTO.getMemberId());
        return ResponseEntity.ok(save);
    }

    @PostMapping("/{competitionCode}/hunting")
    public ResponseEntity<Hunting> saveHunting(
            @PathVariable String competitionCode,
            @RequestBody @Validated FishHuntingDto fishHuntingDto
    ) throws ResourceNotFoundException {
        fishHuntingDto.setCompetitionCode(competitionCode);
        Hunting save = huntingService.save(fishHuntingDto, competitionCode);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/{competitionCode}/rankings")
    public ResponseEntity<List<RankingDTO>> calculateRanking(@PathVariable String competitionCode) throws ResourceNotFoundException {
        List<Ranking> rankings = competitionService.calculateRanking(competitionCode);
        return ResponseEntity.ok(
                rankings.stream()
                        .map(element -> modelMapper.map(element, RankingDTO.class))
                        .toList()
        );
    }

    @GetMapping("/{competitionCode}/members")
    public ResponseEntity<List<MemberDTO>> findAllMembersByCompetitionCode(@PathVariable String competitionCode) {
        List<Member> members = competitionService.findAllMembersByCompetitionCode(competitionCode);
        return ResponseEntity.ok(
                members.stream()
                        .map(member ->{
                            MemberDTO memberDto = modelMapper.map(member, MemberDTO.class);
                            final int[] count = {0};
                            member.getHuntings().stream()
                                    .filter(hunting -> hunting.getCompetition().getCode().equals(competitionCode))
                                    .forEach(hunting ->count[0] += hunting.getNumberOfFish());
                            memberDto.setNbrHunting(count[0]);
                            return memberDto;
                        } )
                        .toList());
    }
}
