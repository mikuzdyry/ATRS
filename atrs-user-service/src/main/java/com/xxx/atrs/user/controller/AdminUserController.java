package com.xxx.atrs.user.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.common.entity.User;
import com.xxx.atrs.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/list")
    public CommonResponse<List<User>> list() {
        return CommonResponse.success(userService.listAll());
    }

    @GetMapping("/detail/{id}")
    public CommonResponse<User> detail(@PathVariable Long id) {
        return CommonResponse.success(userService.getById(id));
    }
}
