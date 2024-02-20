package com.aftasapi.web.rest;


import com.aftasapi.common.PaginatedResponse;
import com.aftasapi.web.dto.MemberDTO;
import com.aftasapi.web.dto.MemberInputDTO;
import com.aftasapi.entity.Member;
import com.aftasapi.web.exception.ResourceNotFoundException;
import com.aftasapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping
    public MemberDTO createMember(@RequestBody @Validated MemberInputDTO memberInputDTO) {
        Member save = memberService.save(modelMapper.map(memberInputDTO, Member.class));
        return modelMapper.map(save, MemberDTO.class);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<MemberDTO>> getAllMembers(
            @ParameterObject Pageable pageable
    ) {
        Page<Member> members = memberService.findAll(pageable);
        PaginatedResponse<MemberDTO> response = PaginatedResponse.<MemberDTO>builder()
                .content(
                        members.stream()
                                .map(member -> modelMapper.map(member, MemberDTO.class))
                                .toList()
                )
                .pageNumber(members.getNumber())
                .totalPages(members.getTotalPages())
                .totalElements(members.getTotalElements())
                .pageSize(members.getSize())
                .build();
        return ResponseEntity.ok().body(response) ;
    }

    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable Long id) throws ResourceNotFoundException {
        Member member = memberService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + id));
        return modelMapper.map(member, MemberDTO.class);
    }

    @PutMapping
    public MemberDTO updateMember(@RequestBody MemberInputDTO updatedMemberDTO) throws ResourceNotFoundException {
        Member update = memberService.update(modelMapper.map(updatedMemberDTO, Member.class));
        return modelMapper.map(update, MemberDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) throws ResourceNotFoundException{
        memberService.deleteMember(id);
    }
}