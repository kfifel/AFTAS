package com.aftasapi.web.rest;


import com.aftasapi.entity.AppUser;
import com.aftasapi.service.UserService;
import com.aftasapi.web.dto.request.UserRequestDto;
import com.aftasapi.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok().body(
                userService.findAll()
                        .stream()
                        .map(usr -> modelMapper.map(usr, UserResponseDto.class))
                        .toList());
    }
}
