package com.acharya.dikshanta.EcomMed.security;

import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .authorities(user.getRole().getAuthorities())
                .build();
    }

    @Transactional
    public UserDetails loadById(UUID id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .password(user.getPassword())
                .authorities(user.getRole().getAuthorities())
                .build();
    }
}
