package com.deep.smartinventoryandordermanagementsystem.config;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import com.deep.smartinventoryandordermanagementsystem.model.User;
import com.deep.smartinventoryandordermanagementsystem.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepo.findUserByUsername("admin") == null) {

            User admin = new User();

            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );
            admin.setRole(Role.ADMIN);

            userRepo.save(admin);

            System.out.println(
                    "Admin account created successfully."
            );
        }
    }
}