package com.food.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.dto.LoginDTO;
import com.food.entity.User;
import com.food.mapper.UserMapper;
import com.food.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(LoginDTO dto) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())
                .eq(User::getStatus, 1));
        if (user == null) throw new RuntimeException("用户不存在或已被禁用");
        if (!dto.getPassword().equals(user.getPassword())) throw new RuntimeException("密码错误");
        StpUtil.login(user.getId());
        StpUtil.getSession().set("user", user);
        return StpUtil.getTokenValue();
    }

    @Override
    public User getCurrentUser() {
        return getById(StpUtil.getLoginIdAsLong());
    }
}
