package com.aftasapi.security.auth;

import com.aftasapi.entity.Member;
import com.aftasapi.repository.MemberRepository;
import com.aftasapi.repository.UserRepository;
import com.aftasapi.security.AuthenticationService;
import com.aftasapi.security.jwt.JwtService;
import com.aftasapi.security.jwt.TokenType;
import com.aftasapi.service.MemberService;
import com.aftasapi.utils.CustomError;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.request.SignInRequest;
import com.aftasapi.web.dto.request.SignUpRequest;
import com.aftasapi.web.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MemberService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository userRepository;

    @Override
    public void signup(SignUpRequest request) {
        Member user = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getFirstName())
                .familyName(request.getLastName())
                .accountNonLocked(false)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
        .build();
        userService.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Member user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return  JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(String  refreshToken) throws ValidationException {
        if(jwtService.isTokenValid(refreshToken, TokenType.REFRESH_TOKEN)) {
            String username = jwtService.extractUserName(refreshToken);
            var user = userRepository.findByEmail(username).orElseThrow(() -> new ValidationException(new CustomError("email", "User not found")));
            var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
            return JwtAuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UnauthorizedException("Refresh token is invalid");
    }

    @Override
    public Member me() {
        return userService.getCurrentUser();
    }
}
