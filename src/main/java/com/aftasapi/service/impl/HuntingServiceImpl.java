package com.aftasapi.service.impl;

import com.aftasapi.web.dto.FishHuntingDto;
import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Fish;
import com.aftasapi.entity.Hunting;
import com.aftasapi.entity.Member;
import com.aftasapi.web.exception.ResourceNotFoundException;
import com.aftasapi.repository.HuntingRepository;
import com.aftasapi.service.CompetitionService;
import com.aftasapi.service.FishService;
import com.aftasapi.service.HuntingService;
import com.aftasapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HuntingServiceImpl implements HuntingService {

    private final HuntingRepository huntingRepository;
    private final MemberService memberService;
    private final CompetitionService competitionService;
    private final FishService fishService;

    @Override
    public Hunting save(FishHuntingDto fishHuntingDto, String competitionCode) throws ResourceNotFoundException {
        Competition competition = competitionService.findByCode(competitionCode)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with code " + competitionCode + " not found"));
        fishHuntingDto.setCompetitionCode(competitionCode);
        Hunting huntingToSaved = canHuntingBeSaved(fishHuntingDto);
        Hunting response;
        Optional<Hunting> findHunt = huntingRepository.findByMemberNumberAndCompetitionCodeAndFishName(
                fishHuntingDto.getMemberId(),
                fishHuntingDto.getCompetitionCode(),
                fishHuntingDto.getFishName());

        if (findHunt.isPresent()) {
            Hunting hunting = findHunt.get();
            hunting.setNumberOfFish(hunting.getNumberOfFish() + 1);
            response = huntingRepository.save(hunting);
        } else {
            huntingToSaved.setNumberOfFish(1);
            response = huntingRepository.save(huntingToSaved);
        }
        competition.getHunting().add(response);
        competitionService.update(competition);
        return response;
    }

    private Hunting canHuntingBeSaved(FishHuntingDto fishHuntingDto) throws ResourceNotFoundException {

        Optional<Competition> competition = competitionService.findByCode(fishHuntingDto.getCompetitionCode());
        if(competition.isEmpty()) {
            throw new ResourceNotFoundException("Competition does not exist with code: " + fishHuntingDto.getCompetitionCode());
        }

        LocalTime startTime = competition.get().getStartTime();
        LocalDate dateOfCompetition = new java.sql.Date(competition.get().getDate().getTime()).toLocalDate();
        LocalDateTime startCompetitionLocalDateTime = dateOfCompetition.atTime(startTime);
        if(LocalDateTime.now().isBefore(startCompetitionLocalDateTime)) {
            throw new IllegalArgumentException("Competition has not started yet");
        }

        LocalTime endTime = competition.get().getEndTime();
        LocalDateTime endCompetitionLocalDateTime = dateOfCompetition.atTime(endTime);
        if (LocalDateTime.now().isAfter(endCompetitionLocalDateTime)) {
            throw new IllegalArgumentException("Competition has ended");
        }

        Optional<Fish> fish = fishService.findByName(fishHuntingDto.getFishName());
        if(fish.isEmpty()) {
            throw new ResourceNotFoundException("Fish does not exist with name: " + fishHuntingDto.getFishName());
        }

        Optional<Member> member = memberService.findByNumber(fishHuntingDto.getMemberId());
        if(member.isEmpty()) {
            throw new ResourceNotFoundException("Member does not exist with id: " + fishHuntingDto.getMemberId());
        }

        if(fishHuntingDto.getFishWeight() < fish.get().getAverageWeight()) {
            throw new IllegalArgumentException("Fish weight must be greater than average weight: " + fish.get().getAverageWeight());
        }

        return Hunting.builder()
                .competition(competition.get())
                .fish(fish.get())
                .member(member.get())
                .build();
    }
}
