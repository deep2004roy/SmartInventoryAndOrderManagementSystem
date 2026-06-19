package com.deep.smartinventoryandordermanagementsystem.dto.auth;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Role role;
}
