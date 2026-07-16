package com.food.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AiChatDTO {
    private String sessionId;
    private String message;
    private List<Map<String, Object>> context; // 历史上下文
}
