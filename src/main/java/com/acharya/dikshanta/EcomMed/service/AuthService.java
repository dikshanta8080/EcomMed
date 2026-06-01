package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.request.LoginRequest;
import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.LoginResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    public UserResponse registerUser(RegistrationRequest request) {
        return userService.createUser(request);

    }

    @Transactional
    public LoginResponse doLogin(LoginRequest request) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

            UserPrincipal userPrincipal =
                    (UserPrincipal) authentication.getPrincipal();

            String token = jwtService.getJwt(userPrincipal);

            return LoginResponse.builder()
                    .id(userPrincipal.getId())
                    .name(userPrincipal.getName())
                    .email(userPrincipal.getUsername())
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }


}