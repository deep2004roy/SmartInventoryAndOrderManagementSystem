package com.deep.smartinventoryandordermanagementsystem.dto.auth;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
