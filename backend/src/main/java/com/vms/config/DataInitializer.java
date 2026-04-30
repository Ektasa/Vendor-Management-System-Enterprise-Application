package com.vms.config;

import com.vms.entity.User;
import com.vms.entity.User.Role;
import com.vms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create Admin
            User admin = User.builder()
                    .email("admin@vms.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Admin")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);

            // Create Manager
            User manager = User.builder()
                    .email("manager@vms.com")
                    .password(passwordEncoder.encode("manager123"))
                    .fullName("Approval Manager")
                    .role(Role.MANAGER)
                    .enabled(true)
                    .build();
            userRepository.save(manager);

            // Create Vendor
            User vendor = User.builder()
                    .email("vendor@vms.com")
                    .password(passwordEncoder.encode("vendor123"))
                    .fullName("Test Vendor")
                    .role(Role.VENDOR)
                    .enabled(true)
                    .build();
            userRepository.save(vendor);

            log.info("Default users created: admin@vms.com, manager@vms.com, vendor@vms.com");
        }
    }
}