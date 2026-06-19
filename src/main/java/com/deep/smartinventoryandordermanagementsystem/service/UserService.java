package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.user.LoginRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.LoginResponse;
import com.deep.smartinventoryandordermanagementsystem.dto.user.RegisterRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.user.UserResponseDTO;
import com.deep.smartinventoryandordermanagementsystem.exception.InvalidCredentialsException;
import com.deep.smartinventoryandordermanagementsystem.exception.UserAlreadyExistsException;
import com.deep.smartinventoryandordermanagementsystem.exception.UserNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Role;
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

    public UserResponseDTO register(RegisterRequest request){
        if (userRepo.findUserByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        User savedUser = userRepo.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole());

    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findUserByUsername(request.getUsername());
        if(user == null){
            throw new UserNotFoundException("User not found");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches){
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getRole());
    }
}
