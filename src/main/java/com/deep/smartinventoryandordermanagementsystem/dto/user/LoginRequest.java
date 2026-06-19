package com.deep.smartinventoryandordermanagementsystem.dto.user;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private Role role;
}
