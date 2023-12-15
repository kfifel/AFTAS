package com.aftasapi.service.impl;

import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.repository.MemberRepository;
import com.aftasapi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        canMemberBeSaved(member);
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
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    @Override
    public List<Member> findAllMembersWithHuntingForCompetition(String competitionCode) {
        return memberRepository.findAllByRankingCompetitionCode(competitionCode);
    }
}
