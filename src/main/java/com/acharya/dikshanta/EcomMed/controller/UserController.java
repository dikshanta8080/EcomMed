package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.PageableRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> findUsers(
            @ModelAttribute PageableRequest pageableRequest,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email
    ) {
        PagedResponse<UserResponse> aLlUsers = userService.getALlUsers(pageableRequest.toPageable(), email, name);
        return ResponseEntity.ok(ApiResponse.success(aLlUsers, "User fetched Successfully"));
    }
}
