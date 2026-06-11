package com.xxx.atrs.service;

import com.xxx.atrs.dto.LoginRequest;
import com.xxx.atrs.dto.RegisterRequest;
import com.xxx.atrs.dto.UserProfileDTO;
import com.xxx.atrs.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    User login(LoginRequest request);
    User getById(Long id);
    void updateProfile(Long userId, UserProfileDTO dto);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
