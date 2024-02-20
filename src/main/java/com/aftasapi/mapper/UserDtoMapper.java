package com.aftasapi.mapper;

import com.aftasapi.entity.AppUser;
import com.aftasapi.entity.Role;
import com.aftasapi.dto.request.UserRequestDto;
import com.aftasapi.dto.response.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {



    private UserDtoMapper() {
    }

    public static AppUser toEntity(UserRequestDto userDto) {
        List<Role> roles = new ArrayList<>();
        if(userDto.getAuthorities() != null){
            for (String role : userDto.getAuthorities()) {
                roles.add(Role.builder().name(role).build());
            }
        }
        return AppUser.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(roles)
                .build();
    }

    public static UserResponseDto toDto(AppUser user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .verifiedAt(user.getVerifiedAt())
                .authorities(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
