package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.enums.Role;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.mappers.UserMapper;
import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(MessageConstants.UserConstants.USER_ALREADY_EXISTS);
        }
        User customer = userMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setRole(Role.CUSTOMER);
        return userMapper.toResponse(userRepository.save(customer));
    }
}
