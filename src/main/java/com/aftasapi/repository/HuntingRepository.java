package com.aftasapi.repository;

import com.aftasapi.entity.Hunting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HuntingRepository extends JpaRepository<Hunting, Long> {
    Optional<Hunting> findByMemberNumberAndCompetitionCodeAndFishName(Long memberId, String competitionCode, String fishName);

}