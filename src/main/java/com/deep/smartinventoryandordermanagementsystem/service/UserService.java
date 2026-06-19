package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.auth.LoginRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.auth.LoginResponse;
import com.deep.smartinventoryandordermanagementsystem.dto.auth.RegisterRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.auth.UserResponseDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.user.ChangePasswordRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.user.UpdateProfileRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.user.UserProfileDTO;
import com.deep.smartinventoryandordermanagementsystem.exception.InvalidCredentialsException;
import com.deep.smartinventoryandordermanagementsystem.exception.UserAlreadyExistsException;
import com.deep.smartinventoryandordermanagementsystem.exception.UserNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Role;
import com.deep.smartinventoryandordermanagementsystem.model.User;
import com.deep.smartinventoryandordermanagementsystem.repository.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void changePassword(ChangePasswordRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        boolean matches = passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new InvalidCredentialsException(
                    "Current password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepo.save(user);
    }

    public UserProfileDTO getCurrentUser() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        UserProfileDTO dto = new UserProfileDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());

        return dto;
    }

    public UserProfileDTO updateProfile(
            UpdateProfileRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepo.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        User savedUser = userRepo.save(user);

        UserProfileDTO dto = new UserProfileDTO();

        dto.setId(savedUser.getId());
        dto.setUsername(savedUser.getUsername());
        dto.setEmail(savedUser.getEmail());
        dto.setFullName(savedUser.getFullName());
        dto.setPhoneNumber(savedUser.getPhoneNumber());
        dto.setAddress(savedUser.getAddress());
        dto.setRole(savedUser.getRole());

        return dto;
    }
}
