package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ai_generate_resource")
public class AiGenerateResource {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private String generateType; // recipe/poster/report/analysis
    private Integer status;
    private String modelInfo;
    private LocalDateTime createTime;
}
