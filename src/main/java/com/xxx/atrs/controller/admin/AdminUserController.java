package com.xxx.atrs.controller.admin;

import com.xxx.atrs.entity.User;
import com.xxx.atrs.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        model.addAttribute("users", users);
        return "admin/user/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) user.setPassword(null);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }
}
