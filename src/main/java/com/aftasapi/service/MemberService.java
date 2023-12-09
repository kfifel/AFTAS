package com.aftasapi.reposity;

import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member member);

    List<Member> findAll();

    Optional<Member> findById(Long memberId);

    Member update(Member updatedMember) throws ResourceNotFoundException;

    void deleteMember(Long memberId);
}
