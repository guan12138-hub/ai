package com.food.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.food.dto.LoginDTO;
import com.food.entity.User;
import com.food.service.UserService;
import com.food.util.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDTO dto) {
        try {
            String token = userService.login(dto);
            return R.ok(token);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }

    @GetMapping("/me")
    public R<User> me() {
        return R.ok(userService.getCurrentUser());
    }

    @PostMapping("/register")
    public R<Void> register(@RequestBody User user) {
        user.setRole(0);
        user.setStatus(1);
        userService.save(user);
        return R.ok();
    }
}
