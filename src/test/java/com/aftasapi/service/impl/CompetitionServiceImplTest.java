package com.aftasapi.service.impl;

import com.aftasapi.entity.*;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.repository.CompetitionRepository;
import com.aftasapi.service.MemberService;
import com.aftasapi.service.RankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompetitionServiceImplTest {

    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private RankService rankService;
    @InjectMocks
    private CompetitionServiceImpl competitionService;

    @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateRanking_Success() throws ResourceNotFoundException {
        // Arrange
        String competitionCode = "yos-2021-01-01";
        Competition competition = Competition.builder()
                .code(competitionCode)
                .amount(200D)
                .date(new java.sql.Date(System.currentTimeMillis()))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(21, 0))
                .build();

        Member member1 = Member.builder().number(1L).build();
        Member member2 = Member.builder().number(2L).build();
        Member member3 = Member.builder().number(3L).build();
        List<Fish> listOfFish = getListOfFish();

        Hunting hunting1 = new Hunting();
        hunting1.setMember(member1);
        hunting1.setNumberOfFish(5);
        hunting1.setFish(listOfFish.get(0)); // 1000 point per 1 => score = 5000

        Hunting hunting2 = new Hunting();
        hunting2.setMember(member2);
        hunting2.setNumberOfFish(3);
        hunting2.setFish(listOfFish.get(1)); // 500 point per 1 => score = 1500

        Hunting hunting3 = new Hunting();
        hunting3.setMember(member3);
        hunting3.setNumberOfFish(1);
        hunting3.setFish(listOfFish.get(2)); // 2000 point per 1 => score = 2000

        competition.setHunting(Arrays.asList(hunting1, hunting2, hunting3));

        // make the members subscribed to the competition

        Ranking ranking1 = new Ranking();
        ranking1.setMember(member1);
        ranking1.setCompetition(competition);

        Ranking ranking2 = new Ranking();
        ranking2.setMember(member2);
        ranking2.setCompetition(competition);

        Ranking ranking3 = new Ranking();
        ranking3.setMember(member3);
        ranking3.setCompetition(competition);

        competition.setRanks(Arrays.asList(ranking1, ranking2, ranking3));

        when(competitionRepository.findByCode(competitionCode)).thenReturn(Optional.of(competition));
        doNothing().when(rankService).saveAll(anyList());

        // Act
        List<Ranking> result = competitionService.calculateRanking(competitionCode);

        // Assertions :
        verify(competitionRepository, times(1)).findByCode(competitionCode);
        verify(rankService, times(1)).saveAll(anyList());

        assertEquals(3, result.size());

        assertEquals(1, result.get(0).getRankTop());
        assertEquals(2, result.get(1).getRankTop());
        assertEquals(3, result.get(2).getRankTop());

        assertEquals(5000, result.get(0).getScore());
        assertEquals(1500, result.get(2).getScore());
        assertEquals(2000, result.get(1).getScore());

        assertEquals(member1, result.get(0).getMember());
        assertEquals(member2, result.get(2).getMember());
        assertEquals(member3, result.get(1).getMember());

    }


    private List<Fish> getListOfFish() {

        List<Fish> fishList = new ArrayList<>();
        fishList.add(Fish.builder().name("Bass").averageWeight(3.0D).level(Level.builder().code(2L).point(1000).build()).build());
        fishList.add(Fish.builder().name("Tuna").averageWeight(1.0D).level(Level.builder().code(1L).point(500).build()).build());
        fishList.add(Fish.builder().name("Cod").averageWeight(8.0D).level(Level.builder().code(4L).point(2000).build()).build());

        return fishList;
    }
}