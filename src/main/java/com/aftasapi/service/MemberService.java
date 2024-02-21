package com.aftasapi.service;

import com.aftasapi.entity.Member;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.RoleDto;
import com.aftasapi.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member save(Member member);

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    UserDetailsService userDetailsService();

    Member findByUsername(String username);

    Optional<Member> findByIdentityNumber(String identityNumber);

    Member update(Member updatedMember) throws ResourceNotFoundException;

    void delete(Long memberId) throws ResourceNotFoundException;

    boolean existsById(Long memberId);

    List<String> getAuthorities();

    public Member getCurrentUser();

    List<Member> findAllMembersWithHuntingForCompetition(String competitionCode);

    void revokeRole(Long id, List<RoleDto> roles) throws ValidationException;

    void assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException;

}
