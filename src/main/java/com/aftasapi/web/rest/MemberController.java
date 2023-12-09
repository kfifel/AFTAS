package com.aftasapi.web.rest;


import com.aftasapi.dto.MemberDTO;
import com.aftasapi.dto.MemberInputDTO;
import com.aftasapi.entity.Member;
import com.aftasapi.exception.ResourceNotFoundException;
import com.aftasapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping
    public MemberDTO createMember(@RequestBody MemberInputDTO memberInputDTO) {
        Member save = memberService.save(modelMapper.map(memberInputDTO, Member.class));
        return modelMapper.map(save, MemberDTO.class);
    }

    @GetMapping
    public List<MemberDTO> getAllMembers() {
        List<Member> all = memberService.findAll();
        return all.stream().map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable Long id) throws ResourceNotFoundException {
        Member member = memberService.findById(id);
        return modelMapper.map(member, MemberDTO.class);
    }

    @PutMapping
    public MemberDTO updateMember(@RequestBody MemberInputDTO updatedMemberDTO) throws ResourceNotFoundException {
        Member update = memberService.update(modelMapper.map(updatedMemberDTO, Member.class));
        return modelMapper.map(update, MemberDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}