package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.LoginRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.LoginResponse;
import com.deep.smartinventoryandordermanagementsystem.model.User;
import com.deep.smartinventoryandordermanagementsystem.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findUserByUsername(request.getUsername());
        if(user == null){
            throw new RuntimeException("User not found");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches){
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getRole());
    }
}
