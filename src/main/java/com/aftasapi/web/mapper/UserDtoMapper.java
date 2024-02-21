package com.aftasapi.web.mapper;

import com.aftasapi.entity.Member;
import com.aftasapi.entity.Role;
import com.aftasapi.web.dto.request.UserRequestDto;
import com.aftasapi.web.dto.response.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {



    private UserDtoMapper() {
    }

    public static Member toEntity(UserRequestDto userDto) {
        List<Role> roles = new ArrayList<>();
        if(userDto.getAuthorities() != null){
            for (String role : userDto.getAuthorities()) {
                roles.add(Role.builder().name(role).build());
            }
        }
        return Member.builder()
                .name(userDto.getFirstName())
                .familyName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(roles)
                .build();
    }

    public static UserResponseDto toDto(Member user) {
        return UserResponseDto.builder()
                .id(user.getNumber())
                .name(user.getName())
                .familyName(user.getFamilyName())
                .email(user.getEmail())
                .accessionDate(user.getAccessionDate())
                .nationality(user.getNationality())
                .identityNumber(user.getIdentityNumber())
                .createdAt(user.getCreatedAt())
                .verifiedAt(user.getVerifiedAt())
                .authorities(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
