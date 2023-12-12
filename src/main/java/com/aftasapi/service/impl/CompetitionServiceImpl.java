package com.aftasapi.service.impl;

import com.aftasapi.entity.Competition;
import com.aftasapi.repository.CompetitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements com.aftasapi.service.CompetitionService {

    private final CompetitionRepository competitionRepository;

    @Override
    public List<Competition> findAll(Pageable pageable) {
        return competitionRepository.findAll(pageable).stream().toList();
    }

    @Override
    public Competition save(Competition competition) throws IllegalArgumentException {
       canCompetitionBeSaved(competition);
        String generateCode = generateCode(competition);
        competition.setCode(generateCode);
        competition.setNumberOfParticipant(0);
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
    }

    @Override
    public Optional<Competition> findByDate(Date date) {
        return competitionRepository.findByDate(date);
    }

    @Override
    public Optional<Competition> findByCode(String code) {
        return competitionRepository.findByCode(code);
    }

}
