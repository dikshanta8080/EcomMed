package com.acharya.dikshanta.EcomMed.loaders;

import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import com.acharya.dikshanta.EcomMed.utils.Utils;
import com.acharya.dikshanta.EcomMed.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class AdminLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;

    @Override
    public void run(String... args) throws Exception {
        Utils.Admin admin = utils.getAdmin();
        if (!userRepository.existsByEmail(admin.getUsername())) {
            User user = User.builder()
                    .name(admin.getName())
                    .email(admin.getUsername())
                    .password(passwordEncoder.encode(admin.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }
}
