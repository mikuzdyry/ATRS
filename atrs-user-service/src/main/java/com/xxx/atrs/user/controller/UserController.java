package com.xxx.atrs.user.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.dto.*;
import com.xxx.atrs.common.entity.User;
import com.xxx.atrs.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public CommonResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return CommonResponse.success("注册成功");
    }

    @PostMapping("/login")
    public CommonResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return CommonResponse.success(Map.of("token", token));
    }

    @GetMapping("/profile")
    public CommonResponse<User> profile(@RequestHeader("X-User-Id") Long userId) {
        return CommonResponse.success(userService.getById(userId));
    }

    @PutMapping("/profile")
    public CommonResponse<String> updateProfile(@RequestHeader("X-User-Id") Long userId,
                                                 @RequestBody UserProfileDTO dto) {
        userService.updateProfile(userId, dto);
        return CommonResponse.success("更新成功");
    }

    @PutMapping("/password")
    public CommonResponse<String> changePassword(@RequestHeader("X-User-Id") Long userId,
                                                  @RequestBody Map<String, String> body) {
        userService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return CommonResponse.success("修改成功");
    }

    // ---- 内部调用接口 ----
    @GetMapping("/internal/{id}")
    public CommonResponse<User> getUserById(@PathVariable Long id) {
        return CommonResponse.success(userService.getById(id));
    }
}
