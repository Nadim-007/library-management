package com.library.config;

import com.library.entity.AppUser;
import com.library.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Create ADMIN if not exists
        if (appUserRepository.findByUsername("admin").isEmpty()) {
            appUserRepository.save(AppUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .role("ROLE_ADMIN")
                .enabled(true)
                .build());
            System.out.println("✅ Default ADMIN created: admin / admin123");
        }

        // Create USER if not exists
        if (appUserRepository.findByUsername("user").isEmpty()) {
            appUserRepository.save(AppUser.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .role("ROLE_USER")
                .enabled(true)
                .build());
            System.out.println("✅ Default USER created: user / user123");
        }
    }
}