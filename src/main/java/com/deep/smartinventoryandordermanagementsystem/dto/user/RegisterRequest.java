package com.deep.smartinventoryandordermanagementsystem.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    @NotBlank
    private String email;
    private String password;
}
