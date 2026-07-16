package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Integer role;       // 0-商户 1-管理员
    private String avatar;
    private Integer status;     // 0-禁用 1-启用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
