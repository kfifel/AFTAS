package com.aftasapi.service;

import com.aftasapi.entity.Member;
import com.aftasapi.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member member);

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByIdentityNumber(String identityNumber);

    Member update(Member updatedMember) throws ResourceNotFoundException;

    void deleteMember(Long memberId) throws ResourceNotFoundException;

    boolean existsByNumber(Long memberId);

    List<Member> findAllMembersWithHuntingForCompetition(String competitionCode);

    Optional<Member> findByNumber(Long memberId);
}
