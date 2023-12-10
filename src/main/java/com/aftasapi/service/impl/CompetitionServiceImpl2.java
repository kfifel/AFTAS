package com.aftasapi.service.impl;

import com.aftasapi.entity.Competition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("competitionServiceImpl2")
public class CompetitionServiceImpl2 implements com.aftasapi.service.CompetitionService {

    private final CompetitionServiceImpl competitionServiceImpl;

    public CompetitionServiceImpl2(
            @Qualifier("competitionServiceImpl") CompetitionServiceImpl competitionServiceImpl) {
        this.competitionServiceImpl = competitionServiceImpl;
    }


    @Override
    public List<Competition> findAll(Pageable pageable) {
        return competitionServiceImpl.findAll(pageable);
    }

    @Override
    public Competition save(Competition competition) throws IllegalArgumentException {
        String s = generateCode(competition);
        competition.setCode(s);
        return competitionServiceImpl.save(competition);
    }

    private String generateCode(Competition competition) {
        // the code should have the 3 first letters of the competition name + the date of the competition
        StringBuilder code = new StringBuilder();
        code.append(competition.getLocation(), 0, 3);
        code.append("-");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String formattedDate = dateFormat.format(competition.getDate());
        code.append(formattedDate);
        return code.toString();
    }

    @Override
    public Optional<Competition> findByDate(Date date) {
        return competitionServiceImpl.findByDate(date);
    }

    @Override
    public Optional<Competition> findByCode(String code) {
        return competitionServiceImpl.findByCode(code);
    }

}
