package com.aftasapi.security.auth;

import com.aftasapi.entity.AppUser;
import com.aftasapi.repository.UserRepository;
import com.aftasapi.security.AuthenticationService;
import com.aftasapi.security.jwt.JwtService;
import com.aftasapi.security.jwt.TokenType;
import com.aftasapi.service.UserService;
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

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public void signup(SignUpRequest request) {
        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
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
        var user = userRepository.findByEmail(request.getEmail())
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
    public AppUser me() {
        return userService.getCurrentUser();
    }
}
