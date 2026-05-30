package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    public UserResponse registerUser(RegistrationRequest request) {
        return userService.createUser(request);
    }
}
