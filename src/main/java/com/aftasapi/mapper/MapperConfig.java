package com.aftasapi.mapper;


import com.aftasapi.dto.MemberDTO;
import com.aftasapi.dto.MemberInputDTO;
import com.aftasapi.entity.Member;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

    public MemberDTO convertToDto(Member member) {
        return modelMapper().map(member, MemberDTO.class);
    }

    public Member convertToEntity(MemberInputDTO memberInputDTO) {
        return modelMapper().map(memberInputDTO, Member.class);
    }
}
