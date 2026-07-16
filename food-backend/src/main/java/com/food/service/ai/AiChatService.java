package com.food.service.ai;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.entity.AiChatRecord;
import com.food.mapper.AiChatRecordMapper;
import com.food.util.CozeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AiChatService {

    private final CozeUtil cozeUtil;
    private final AiChatRecordMapper chatRecordMapper;

    public AiChatService(CozeUtil cozeUtil, AiChatRecordMapper chatRecordMapper) {
        this.cozeUtil = cozeUtil;
        this.chatRecordMapper = chatRecordMapper;
    }

    @Transactional
    public String chat(Long userId, String sessionId, String message) {
        // 保存用户消息
        AiChatRecord userRecord = new AiChatRecord();
        userRecord.setUserId(userId);
        userRecord.setSessionId(sessionId);
        userRecord.setRole("user");
        userRecord.setContent(message);
        userRecord.setModelType("deepseek");
        userRecord.setCreateTime(LocalDateTime.now());
        chatRecordMapper.insert(userRecord);

        // 调用 DeepSeek LLM
        String reply = cozeUtil.chatWithLLM(
            "你是一位专业的食材管理系统AI助手，擅长回答食材采购、库存管理、菜谱推荐、损耗分析等问题。请给出专业、实用的建议。",
            message
        );

        // 保存 AI 回复
        AiChatRecord aiRecord = new AiChatRecord();
        aiRecord.setUserId(userId);
        aiRecord.setSessionId(sessionId);
        aiRecord.setRole("assistant");
        aiRecord.setContent(reply);
        aiRecord.setModelType("deepseek");
        aiRecord.setCreateTime(LocalDateTime.now());
        chatRecordMapper.insert(aiRecord);

        return reply;
    }

    public List<AiChatRecord> getHistory(Long userId, String sessionId) {
        return chatRecordMapper.selectList(
                new LambdaQueryWrapper<AiChatRecord>()
                        .eq(AiChatRecord::getUserId, userId)
                        .eq(AiChatRecord::getSessionId, sessionId)
                        .orderByAsc(AiChatRecord::getCreateTime));
    }
}
