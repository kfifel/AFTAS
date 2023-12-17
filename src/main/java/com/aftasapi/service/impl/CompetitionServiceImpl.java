package com.aftasapi.service.impl;

import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Member;
import com.aftasapi.entity.Ranking;
import com.aftasapi.entity.embedded.RankingId;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.repository.CompetitionRepository;
import com.aftasapi.service.CompetitionService;
import com.aftasapi.service.MemberService;
import com.aftasapi.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final MemberService memberService;
    private final RankService rankService;

    @Override
    @Transactional
    public Page<Competition> findAll(Pageable pageable, String query) {
        Page<Competition> page;
        if(query != null && !query.isEmpty() && !query.isBlank()) {
            query = query.trim();
            page = competitionRepository.findAllByLocationContainsIgnoreCaseOrCodeContainsIgnoreCase("%" + query + "%", "%" + query + "%", pageable);
        } else
            page = competitionRepository.findAll(pageable);
        return page;
    }

    @Override
    public Competition save(Competition competition) throws IllegalArgumentException {
       canCompetitionBeSaved(competition);
        String generateCode = generateCode(competition);
        competition.setCode(generateCode);
        competition.setNumberOfParticipant(0);
        return competitionRepository.save(competition);
    }

    @Override
    public Competition update(Competition competition) throws ResourceNotFoundException {
        if(competition.getCode() == null || !competitionRepository.existsById(competition.getCode()))
            throw new ResourceNotFoundException("Competition code is required");
        return competitionRepository.save(competition);
    }

    private String generateCode(Competition competition) {
        // the code should have the 3 first letters of the competition name + the date of the competition iml-dd-mm-yy
        StringBuilder code = new StringBuilder();
        code.append(competition.getLocation(), 0, 3);
        code.append("-");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String formattedDate = dateFormat.format(competition.getDate());
        code.append(formattedDate);
        return code.toString();
    }

    private void canCompetitionBeSaved(Competition competition) throws IllegalArgumentException {
        LocalDateTime dateOfCompetition = LocalDateTime.ofInstant(competition.getDate().toInstant(), ZoneId.systemDefault());

        if(dateOfCompetition.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Competition date cannot be in the past");
        }

        if(dateOfCompetition.isBefore(LocalDateTime.now().plusDays(2))) {
            throw new IllegalArgumentException("Competition date cannot be less than 48 hours from now");
        }

        if (findByCode(competition.getCode()).isPresent()) {
            throw new IllegalArgumentException("Competition already exists with same code: " + competition.getCode());
        }

        // if a competition already created in the same day  (and same location => en cours de discussion), throw an exception
        if (findByDate(competition.getDate()).isPresent()) {
            throw new IllegalArgumentException("Competition already exists in this date: " + competition.getDate());
        }

        if(competition.getStartTime().isAfter(competition.getEndTime()))
            throw new IllegalArgumentException("Start time cannot be after end time");

        if(competition.getStartTime().isBefore(competition.getEndTime().plusHours(4)))
            throw new IllegalArgumentException("Competition duration cannot be less than 4 hours");
    }

    @Override
    public Optional<Competition> findByDate(Date date) {
        return competitionRepository.findByDate(date);
    }

    @Override
    public Optional<Competition> findByCode(String code) {
        return competitionRepository.findByCode(code);
    }

    @Override
    @Transactional
    public Ranking registerMember(String competitionCode, Long memberId) throws ResourceNotFoundException {
        if(!competitionRepository.existsById(competitionCode) || !memberService.existsById(memberId))
            throw new ResourceNotFoundException("Competition or member not found");

        Competition competition = findByCode(competitionCode)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found with code: "+ competitionCode));

        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        checkIfCanRegisterToCompetition(competition, member);

        competition.setNumberOfParticipant(competition.getNumberOfParticipant() + 1);
        Ranking rank = createRank(competition, member);
        competition.getRanks().add(rank);
        competitionRepository.save(competition);

        return rank;
    }

    @Override
    @Transactional
    public List<Ranking> calculateRanking(String competitionCode) throws ResourceNotFoundException {
        Competition competition = findByCode(competitionCode)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
        List<Ranking> rankings = new ArrayList<>(competition.getRanks());
        rankings.forEach(ranking -> {
            int []score = {0};
            competition.getHunting().forEach(hunting -> {
                if(hunting.getMember().equals(ranking.getMember())) {
                    score[0] += hunting.getNumberOfFish() * hunting.getFish().getLevel().getPoint();
                }
            });
            ranking.setScore(score[0]);
        });
        rankings.sort((o1, o2) -> o2.getScore() - o1.getScore());
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }
        rankService.saveAll(rankings);
        return rankings;
    }

    @Override
    public List<Member> findAllMembersByCompetitionCode(String competitionCode) {
        if(!competitionRepository.existsById(competitionCode))
            throw new IllegalArgumentException("Competition not found");
        return memberService.findAllMembersWithHuntingForCompetition(competitionCode);
    }

    private Ranking createRank(Competition competition, Member member) {
        Ranking ranking = Ranking.builder()
                .id(
                        RankingId.builder()
                                .competitionCode(competition.getCode())
                                .memberId(member.getNumber())
                                .build()
                )
                .competition(competition)
                .member(member)
                .rank(0)
                .score(0)
                .build();
        return rankService.save(ranking);
    }

    private void checkIfCanRegisterToCompetition(Competition competition, Member member) throws IllegalArgumentException {
        if(competitionRepository.findByCodeAndRanksMember(competition.getCode(), member).isPresent()) {
            throw new IllegalArgumentException("Member already registered to this competition");
        }

        // transfert competition.getDate(), competition.getStartTime() to LocalDateTime
        LocalTime startTime = competition.getStartTime();
        LocalDate dateOfCompetition = new java.sql.Date(competition.getDate().getTime()).toLocalDate();

        LocalDateTime dateTime = dateOfCompetition.atTime(startTime);

        if (!dateTime.isAfter(LocalDateTime.now().plusDays(1))) {
            throw new IllegalArgumentException("Competition already closed");
        }

    }
}
