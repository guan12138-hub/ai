package com.food.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.food.entity.AiChatRecord;
import com.food.service.ai.AiChatService;
import com.food.util.R;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai/chat")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/send")
    public R<String> send(@RequestParam(required = false) String sessionId,
                          @RequestBody String message) {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }
        String reply = aiChatService.chat(StpUtil.getLoginIdAsLong(), sessionId, message);
        return R.ok(reply);
    }

    @GetMapping("/history")
    public R<List<AiChatRecord>> history(@RequestParam String sessionId) {
        return R.ok(aiChatService.getHistory(StpUtil.getLoginIdAsLong(), sessionId));
    }
}
