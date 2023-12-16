package com.aftasapi.repository;

import com.aftasapi.entity.Competition;
import com.aftasapi.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, String> {
    Optional<Competition> findByDate(Date date);

    Optional<Competition> findByCode(String code);

    Optional<Competition> findByCodeAndRanksMember(String code, Member member);

    @Query("SELECT c FROM Competition c WHERE c.location LIKE %?1%")
    Page<Competition> findAllByLocationContainsIgnoreCase(String query, Pageable pageable);
}