package com.xxx.atrs.controller;

import com.xxx.atrs.common.exception.BusinessException;
import com.xxx.atrs.dto.LoginRequest;
import com.xxx.atrs.dto.RegisterRequest;
import com.xxx.atrs.dto.UserProfileDTO;
import com.xxx.atrs.entity.User;
import com.xxx.atrs.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/user/login")
    public String login(@Valid LoginRequest request, BindingResult bindingResult,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/user/login";
        }
        try {
            User user = userService.login(request);
            session.setAttribute("currentUser", user);
            return "redirect:/";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/login";
        }
    }

    @GetMapping("/user/register")
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("/user/register")
    public String register(@Valid RegisterRequest request, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/user/register";
        }
        try {
            userService.register(request);
            redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
            return "redirect:/user/login";
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/profile")
    public String profilePage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("currentUser");
        User user = userService.getById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/user/profile")
    public String updateProfile(UserProfileDTO dto, HttpSession session, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("currentUser");
        userService.updateProfile(sessionUser.getId(), dto);
        sessionUser.setRealName(dto.getRealName());
        sessionUser.setPhone(dto.getPhone());
        sessionUser.setEmail(dto.getEmail());
        sessionUser.setIdCard(dto.getIdCard());
        session.setAttribute("currentUser", sessionUser);
        redirectAttributes.addFlashAttribute("success", "个人信息已更新");
        return "redirect:/user/profile";
    }
}
