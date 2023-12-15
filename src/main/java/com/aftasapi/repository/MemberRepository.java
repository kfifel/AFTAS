package com.aftasapi.repository;

import com.aftasapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdentityNumber(String identityNumber);

    @Query("SELECT r.member FROM Ranking r WHERE r.competition.code = :competitionCode")
    List<Member> findAllByRankingCompetitionCode(String competitionCode);
}