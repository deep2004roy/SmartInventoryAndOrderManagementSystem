package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}
