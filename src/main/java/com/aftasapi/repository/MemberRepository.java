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

    @Query("SELECT m FROM Member m WHERE m.number IN (SELECT r.member.number FROM Ranking r WHERE r.id.competitionCode = :competitionCode)")
    List<Member> findAllByRankingCompetitionCode(String competitionCode);
}