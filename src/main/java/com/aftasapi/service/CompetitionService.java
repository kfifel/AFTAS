package com.aftasapi.service;

import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Ranking;
import com.aftasapi.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CompetitionService {
    List<Competition> findAll(Pageable pageable);

    Competition save(Competition competition) throws IllegalArgumentException;

    Optional<Competition> findByDate(Date date);

    Optional<Competition> findByCode(String code) throws ResourceNotFoundException;

    Ranking registerMember(String competitionCode, Long memberId) throws ResourceNotFoundException;
}
