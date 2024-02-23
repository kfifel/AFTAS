package com.aftasapi.security.auth;

import com.aftasapi.entity.AppUser;
import com.aftasapi.security.auth.JwtAuthenticationResponse;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.request.SignInRequest;
import com.aftasapi.web.dto.request.SignUpRequest;

public interface AuthenticationService {

    void signup(SignUpRequest request) throws ValidationException;

    JwtAuthenticationResponse signin(SignInRequest request);

    JwtAuthenticationResponse refreshToken(String refreshToken) throws ValidationException;

    AppUser me();
}
