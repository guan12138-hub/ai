package com.food.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.food.entity.User;
import com.food.dto.LoginDTO;

public interface UserService extends IService<User> {
    String login(LoginDTO dto);
    User getCurrentUser();
}
