package com.aftasapi.service.impl;

import com.aftasapi.entity.Member;
import com.aftasapi.entity.Role;
import com.aftasapi.repository.RoleRepository;
import com.aftasapi.security.AuthoritiesConstants;
import com.aftasapi.security.SecurityUtils;
import com.aftasapi.utils.CustomError;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.RoleDto;
import com.aftasapi.web.exception.ResourceNotFoundException;
import com.aftasapi.repository.MemberRepository;
import com.aftasapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository memRoleRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        canMemberBeSaved(member);
        if( SecurityUtils.isAuthenticated() &&
                SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.ROLE_MANAGER, AuthoritiesConstants.ROLE_ADMIN)
        ) {
            member.setAccountNonLocked(true);
        }
        return memberRepository.save(member);
    }

    private void canMemberBeSaved(Member member) {
        if(member.getNumber() != null) {
            throw new IllegalArgumentException("Member already exists");
        }
        if (findByIdentityNumber(member.getIdentityNumber()).isPresent()) {
            throw new IllegalArgumentException("Member with identity number " + member.getIdentityNumber() + " already exists");
        }
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with: " + username)
        );
    }

    @Override
    public Member findByUsername(String username) {
        return findByEmail(username).orElseThrow();
    }

    @Override
    public Optional<Member> findByIdentityNumber(String identityNumber) {
        return memberRepository.findByIdentityNumber(identityNumber);
    }

    @Override
    public Member update(Member updatedMember) throws ResourceNotFoundException {
        Member member = memberRepository.findById(updatedMember.getNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + updatedMember.getNumber()));
        if(! member.getIdentityNumber().equals(updatedMember.getIdentityNumber())) {
            throw new IllegalArgumentException("Identity number cannot be changed");
        }

        return memberRepository.save(updatedMember);
    }

    @Override
    public void delete(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + memberId));
        memberRepository.deleteById(member.getNumber());
    }

    @Override
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    @Override
    public List<String> getAuthorities() {
        return getCurrentUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public Member getCurrentUser() {
        return (Member) SecurityUtils.getCurrentUser();
    }

    @Override
    public List<Member> findAllMembersWithHuntingForCompetition(String competitionCode) {
        return memberRepository.findAllByRankingCompetitionCode(competitionCode);
    }

    @Override
    public void revokeRole(Long id, List<RoleDto> roles) throws ValidationException {
        Optional<Member> userOptional = memberRepository.findById(id);
        if(userOptional.isPresent()){
            Member user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            roles.forEach(roleDto -> roleService.
                    findByName(roleDto.getName()).ifPresent(roleList::add));

            if (new HashSet<>(user.getRoles()).containsAll(roleList)) {
                user.getRoles().removeAll(roleList);
                userRepository.save(user);
            } else {
                throw new ValidationException(CustomError.builder()
                        .field("roles")
                        .message("User does not have all specified roles.")
                        .build());
            }
        }
        else {
            throw new ValidationException(CustomError.builder()
                    .field("user id")
                    .message("User does not exist")
                    .build());
        }
    }

    @Override
    public void assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException {

    }
}
