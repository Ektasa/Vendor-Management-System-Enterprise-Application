package com.vms.config;

import com.vms.entity.User;
import com.vms.entity.User.Role;
import com.vms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create Admin
            User admin = new User();
            admin.setEmail("admin@vms.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Admin");
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);

            // Create Manager
            User manager = new User();
            manager.setEmail("manager@vms.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFullName("Approval Manager");
            manager.setRole(Role.MANAGER);
            manager.setEnabled(true);
            userRepository.save(manager);

            // Create Vendor
            User vendor = new User();
            vendor.setEmail("vendor@vms.com");
            vendor.setPassword(passwordEncoder.encode("vendor123"));
            vendor.setFullName("Test Vendor");
            vendor.setRole(Role.VENDOR);
            vendor.setEnabled(true);
            userRepository.save(vendor);

            System.out.println("Default users created: admin@vms.com, manager@vms.com, vendor@vms.com");
        }
    }
}