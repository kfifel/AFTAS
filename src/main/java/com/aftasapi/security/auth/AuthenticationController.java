package com.aftasapi.security.auth;

import com.aftasapi.entity.AppUser;
import com.aftasapi.entity.Member;
import com.aftasapi.security.AuthenticationService;
import com.aftasapi.utils.ResponseApi;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.request.SignInRequest;
import com.aftasapi.web.dto.request.SignUpRequest;
import com.aftasapi.web.dto.response.UserResponseDto;
import com.aftasapi.web.exception.UnauthorizedException;
import com.aftasapi.web.mapper.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody @Valid SignInRequest credential) {
        JwtAuthenticationResponse result = authenticationService.signin(credential);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseApi<String>> signup(@RequestBody @Valid SignUpRequest register) throws ValidationException {
        authenticationService.signup(register);
        return ResponseEntity.ok(ResponseApi.<String>builder().message("Thank you for regist, wait the manager to approve you!").build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        Member result = authenticationService.me();
        return ResponseEntity.ok(UserDtoMapper.toDto(result));
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request) throws ValidationException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Refresh token is missing");
        }
        String token = authorization.substring(7);
        JwtAuthenticationResponse result = authenticationService.refreshToken(token);
        return ResponseEntity.ok(result);
    }
}
