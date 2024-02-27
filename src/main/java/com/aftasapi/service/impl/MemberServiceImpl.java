package com.aftasapi.service.impl;

import com.aftasapi.entity.Member;
import com.aftasapi.entity.SequenceGenerator;
import com.aftasapi.repository.RoleRepository;
import com.aftasapi.repository.SequenceGeneratorRepository;
import com.aftasapi.security.AuthoritiesConstants;
import com.aftasapi.web.exception.ResourceNotFoundException;
import com.aftasapi.repository.MemberRepository;
import com.aftasapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Member save(Member member) {
        canMemberBeSaved(member); // Validate the member values

        Long nextValue = sequenceGeneratorRepository.findNextValue("member").orElseGet(
                () -> sequenceGeneratorRepository.save(
                        SequenceGenerator.builder()
                                .entity("member")
                                .nextId(1000L)
                                .build()
                ).getNextId()
        );
        member.setAccountNonLocked(true);
        member.setAccountNonExpired(true);
        member.setCredentialsNonExpired(true);
        member.setEnabled(true);
        member.setNumber(nextValue);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        sequenceGeneratorRepository.updateNextIdByEntity("member", ++nextValue);
        member.setRoles(List.of(Objects.requireNonNull(roleRepository.findByName(AuthoritiesConstants.ROLE_MEMBER).orElse(null))));
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
    public void deleteMember(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + memberId));
        memberRepository.deleteById(member.getNumber());
    }

    @Override
    public boolean existsByNumber(Long memberId) {
        return memberRepository.existsByNumber(memberId);
    }

    @Override
    public List<Member> findAllMembersWithHuntingForCompetition(String competitionCode) {
        return memberRepository.findAllByRankingCompetitionCode(competitionCode);
    }

    @Override
    public Optional<Member> findByNumber(Long number) {
        return memberRepository.findByNumber(number);
    }
}
