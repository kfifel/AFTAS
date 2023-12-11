package com.aftasapi.service.impl;

import com.aftasapi.dto.FishHuntingDto;
import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Fish;
import com.aftasapi.entity.Hunting;
import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.repository.HuntingRepository;
import com.aftasapi.service.CompetitionService;
import com.aftasapi.service.FishService;
import com.aftasapi.service.HuntingService;
import com.aftasapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HuntingServiceImpl implements HuntingService {

    private final HuntingRepository huntingRepository;
    private final MemberService memberService;
    private final CompetitionService competitionService;
    private final FishService fishService;

    @Override
    public Hunting save(FishHuntingDto fishHuntingDto) throws ResourceNotFoundException {
        Hunting huntingToSaved = canHuntingBeSaved(fishHuntingDto);

        Optional<Hunting> findHunt = huntingRepository.findByMemberNumberAndCompetitionCodeAndFishName(
                fishHuntingDto.getMemberId(),
                fishHuntingDto.getCompetitionCode(),
                fishHuntingDto.getFishName());

        if (findHunt.isPresent()) {
            Hunting hunting = findHunt.get();
            hunting.setNumberOfFish(hunting.getNumberOfFish() + 1);
            return huntingRepository.save(hunting);
        }

       huntingToSaved.setNumberOfFish(1);
        return huntingRepository.save(huntingToSaved);
    }

    private Hunting canHuntingBeSaved(FishHuntingDto fishHuntingDto) throws ResourceNotFoundException {

        Optional<Fish> fish = fishService.findByName(fishHuntingDto.getFishName());
        if(fish.isEmpty()) {
            throw new ResourceNotFoundException("Fish does not exist with name: " + fishHuntingDto.getFishName());
        }

        Optional<Member> member = memberService.findById(fishHuntingDto.getMemberId());
        if(member.isEmpty()) {
            throw new ResourceNotFoundException("Member does not exist with id: " + fishHuntingDto.getMemberId());
        }

        Optional<Competition> competition = competitionService.findByCode(fishHuntingDto.getCompetitionCode());
        if(competition.isEmpty()) {
            throw new ResourceNotFoundException("Competition does not exist with code: " + fishHuntingDto.getCompetitionCode());
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
