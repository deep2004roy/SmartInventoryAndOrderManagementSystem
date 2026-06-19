package com.deep.smartinventoryandordermanagementsystem.dto.user;

import com.deep.smartinventoryandordermanagementsystem.model.Role;
import lombok.Data;

@Data
public class UserProfileDTO {
    private Long id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String address;

    private Role role;
}
