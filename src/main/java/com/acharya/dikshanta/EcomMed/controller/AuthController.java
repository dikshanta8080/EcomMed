package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.LoginRequest;
import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.LoginResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegistrationRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success(userResponse, MessageConstants.AuthConstants.REGISTRATION_SUCCESSFUL));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.doLogin(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, MessageConstants.AuthConstants.LOGIN_SUCCESSFUL));
    }
}
