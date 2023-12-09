package com.aftasapi.reposity.impl;

import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.repository.MemberRepository;
import com.aftasapi.reposity.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return memberRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member update(Member updatedMember) throws ResourceNotFoundException {
        return memberRepository.findById(updatedMember.getId())
                .map(member -> {
                    member.setName(updatedMember.getName());
                    member.setFamilyName(updatedMember.getFamilyName());
                    member.setAccessionDate(updatedMember.getAccessionDate());
                    member.setNationality(updatedMember.getNationality());
                    member.setIdentityDocumentType(updatedMember.getIdentityDocumentType());
                    member.setIdentityNumber(updatedMember.getIdentityNumber());
                    return memberRepository.save(member);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + updatedMember.getId()));
    }

    @Override
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
