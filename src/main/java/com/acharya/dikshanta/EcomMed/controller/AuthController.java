package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid RegistrationRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success(userResponse, MessageConstants.AuthConstants.REGISTRATION_SUCCESSFUL));
    }
}
