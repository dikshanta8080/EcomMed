package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.enums.Role;
import com.acharya.dikshanta.EcomMed.events.UserRegisteredEvent;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.mappers.UserMapper;
import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserResponse createUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(MessageConstants.UserConstants.USER_ALREADY_EXISTS);
        }
        User customer = userMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setRole(Role.CUSTOMER);
        User savedUser = userRepository.save(customer);
        UserRegisteredEvent registeredEvent = getEvent(savedUser);
        eventPublisher.publishEvent(registeredEvent);
        return userMapper.toResponse(savedUser);
    }

    private UserRegisteredEvent getEvent(User savedUser) {
        return UserRegisteredEvent.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> getALlUsers(Pageable pageable, String email, String search) {
        Page<User> pagedUsers = userRepository.findPagedUsers(search, email, pageable);
        Page<UserResponse> userResponses = pagedUsers.map(userMapper::toResponse);
        userResponses.forEach(System.out::println);
        return PagedResponse.toPagedResponse(userResponses);

    }
}
