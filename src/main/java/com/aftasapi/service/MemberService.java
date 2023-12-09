package com.aftasapi.service;

import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member member);

    List<Member> findAll();

    Member findById(Long memberId) throws ResourceNotFoundException;

    Member update(Member updatedMember) throws ResourceNotFoundException;

    void deleteMember(Long memberId);
}
