package com.food.service.ai;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.entity.AiGenerateResource;
import com.food.mapper.AiGenerateResourceMapper;
import com.food.mapper.FoodStockMapper;
import com.food.util.CozeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiGenerateService {

    private final CozeUtil cozeUtil;
    private final AiGenerateResourceMapper resourceMapper;
    private final FoodStockMapper stockMapper;

    public AiGenerateService(CozeUtil cozeUtil, AiGenerateResourceMapper resourceMapper,
                             FoodStockMapper stockMapper) {
        this.cozeUtil = cozeUtil;
        this.resourceMapper = resourceMapper;
        this.stockMapper = stockMapper;
    }

    @Transactional
    public AiGenerateResource generateRecipe(Long userId, String prompt) {
        String systemPrompt = "你是一位专业厨师，请根据提供的食材清单，创作一道详细菜谱，包含：菜名、所需食材、调料、制作步骤、烹饪技巧。";
        String aiContent = cozeUtil.chatWithLLM(systemPrompt, prompt);

        AiGenerateResource resource = new AiGenerateResource();
        resource.setUserId(userId);
        resource.setTitle("AI菜谱-" + LocalDate.now());
        resource.setContent(aiContent);
        resource.setGenerateType("recipe");
        resource.setStatus(1);
        resource.setModelInfo("deepseek-chat");
        resource.setCreateTime(LocalDateTime.now());
        resourceMapper.insert(resource);
        return resource;
    }

    @Transactional
    public AiGenerateResource generatePurchaseReport(Long userId) {
        List<com.food.entity.FoodStock> lowStock = stockMapper.selectWarningStock(userId);
        String stockData = lowStock.stream()
                .map(s -> s.getName() + "(" + s.getCategory() + "): 当前库存" + s.getStockQuantity() + s.getUnit()
                        + ", 预警阈值" + s.getWarningThreshold() + s.getUnit())
                .collect(Collectors.joining("; "));

        String systemPrompt = "你是一位餐饮采购经理，请根据以下库存预警数据，生成一份详细的采购计划报告，包含：采购清单、建议数量、优先级排序。";
        String aiContent = cozeUtil.chatWithLLM(systemPrompt, stockData);

        AiGenerateResource resource = new AiGenerateResource();
        resource.setUserId(userId);
        resource.setTitle("AI采购报表-" + LocalDate.now());
        resource.setContent(aiContent);
        resource.setGenerateType("report");
        resource.setStatus(1);
        resource.setModelInfo("deepseek-chat");
        resource.setCreateTime(LocalDateTime.now());
        resourceMapper.insert(resource);
        return resource;
    }

    @Transactional
    public AiGenerateResource generateLossReport(Long userId, String lossData) {
        String systemPrompt = "你是一位餐饮成本分析师，请根据以下食材损耗数据，分析损耗原因和趋势，提出降低成本的具体建议。";
        String aiContent = cozeUtil.chatWithLLM(systemPrompt, lossData);

        AiGenerateResource resource = new AiGenerateResource();
        resource.setUserId(userId);
        resource.setTitle("AI损耗分析报告-" + LocalDate.now());
        resource.setContent(aiContent);
        resource.setGenerateType("analysis");
        resource.setStatus(1);
        resource.setModelInfo("deepseek-chat");
        resource.setCreateTime(LocalDateTime.now());
        resourceMapper.insert(resource);
        return resource;
    }

    @Transactional
    public AiGenerateResource generateImage(Long userId, String prompt) {
        String imageUrl = cozeUtil.generateImage(prompt);

        AiGenerateResource resource = new AiGenerateResource();
        resource.setUserId(userId);
        resource.setTitle("AI绘图-" + LocalDate.now());
        resource.setContent(prompt);
        resource.setImageUrl(imageUrl);
        resource.setGenerateType("poster");
        resource.setStatus(imageUrl != null ? 1 : 2);
        resource.setModelInfo("cogview-3-flash");
        resource.setCreateTime(LocalDateTime.now());
        resourceMapper.insert(resource);
        return resource;
    }

    public List<AiGenerateResource> getHistory(Long userId, String type) {
        return resourceMapper.selectList(
                new LambdaQueryWrapper<AiGenerateResource>()
                        .eq(AiGenerateResource::getUserId, userId)
                        .eq(type != null, AiGenerateResource::getGenerateType, type)
                        .orderByDesc(AiGenerateResource::getCreateTime));
    }
}
