package com.deep.smartinventoryandordermanagementsystem.dto.auth;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Role role;
}
