package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ai_chat_record")
public class AiChatRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String sessionId;
    private String role;
    private String content;
    private String modelType;
    private LocalDateTime createTime;
}
