package com.food.service.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.entity.*;
import com.food.mapper.*;
import com.food.util.CozeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiAnalysisService {

    private final CozeUtil cozeUtil;
    private final FoodLossDataMapper lossDataMapper;
    private final FoodStockMapper stockMapper;
    private final PurchaseOrderMapper orderMapper;
    private final AiAnalysisReportMapper reportMapper;

    public AiAnalysisService(CozeUtil cozeUtil, FoodLossDataMapper lossDataMapper,
                             FoodStockMapper stockMapper, PurchaseOrderMapper orderMapper,
                             AiAnalysisReportMapper reportMapper) {
        this.cozeUtil = cozeUtil;
        this.lossDataMapper = lossDataMapper;
        this.stockMapper = stockMapper;
        this.orderMapper = orderMapper;
        this.reportMapper = reportMapper;
    }

    @Transactional
    public AiAnalysisReport analyzeLossTrend(Long userId, String startDate, String endDate) {
        List<Map<String, Object>> trendData = lossDataMapper.selectLossTrend(userId, startDate, endDate);
        String dataStr = JSON.toJSONString(trendData);

        String systemPrompt = "你是一位餐饮数据分析师，根据以下月度损耗趋势数据，分析损耗变化规律，指出异常月份，给出改进建议。";
        String aiSummary = cozeUtil.chatWithLLM(systemPrompt, dataStr);

        JSONObject chartConfig = new JSONObject();
        chartConfig.put("type", "line");
        chartConfig.put("title", "损耗趋势分析");
        JSONObject xAxis = new JSONObject();
        xAxis.put("type", "category");
        xAxis.put("data", trendData.stream().map(m -> m.get("month")).collect(Collectors.toList()));
        chartConfig.put("xAxis", xAxis);

        AiAnalysisReport report = new AiAnalysisReport();
        report.setUserId(userId);
        report.setReportTitle("损耗趋势分析-" + LocalDate.now());
        report.setReportType("loss_trend");
        report.setReportData(dataStr);
        report.setSummary(aiSummary);
        report.setChartConfig(chartConfig.toJSONString());
        report.setPeriodStart(LocalDate.parse(startDate));
        report.setPeriodEnd(LocalDate.parse(endDate));
        report.setCreateTime(LocalDateTime.now());
        reportMapper.insert(report);
        return report;
    }

    @Transactional
    public AiAnalysisReport analyzeCost(Long userId) {
        List<FoodStock> stocks = stockMapper.selectList(
                new LambdaQueryWrapper<FoodStock>().eq(FoodStock::getUserId, userId).eq(FoodStock::getStatus, 1));

        String stockSummary = stocks.stream()
                .map(s -> s.getName() + " 库存:" + s.getStockQuantity() + s.getUnit()
                        + " 单价:" + (s.getPurchasePrice() != null ? s.getPurchasePrice() : "未设置")
                        + " 预计价值:" + (s.getPurchasePrice() != null ? s.getPurchasePrice().multiply(s.getStockQuantity()) : "未知"))
                .collect(Collectors.joining("; "));

        String systemPrompt = "你是一位餐饮成本分析师，根据以下库存数据和单价，分析当前库存成本分布，建议优化采购策略，减少资金占用。";
        String aiSummary = cozeUtil.chatWithLLM(systemPrompt, stockSummary);

        AiAnalysisReport report = new AiAnalysisReport();
        report.setUserId(userId);
        report.setReportTitle("库存成本分析-" + LocalDate.now());
        report.setReportType("cost_analysis");
        report.setReportData(stockSummary);
        report.setSummary(aiSummary);

        JSONObject chartConfig = new JSONObject();
        chartConfig.put("type", "pie");
        chartConfig.put("title", "库存成本分布");
        report.setChartConfig(chartConfig.toJSONString());

        report.setCreateTime(LocalDateTime.now());
        reportMapper.insert(report);
        return report;
    }

    @Transactional
    public AiAnalysisReport analyzeWarning(Long userId) {
        List<FoodStock> warningStocks = stockMapper.selectWarningStock(userId);
        List<FoodStock> expiringStocks = stockMapper.selectExpiringSoon(userId);

        String warningData = "【低库存预警】" +
                warningStocks.stream().map(s -> s.getName() + " 库存:" + s.getStockQuantity() + s.getUnit()
                        + " 阈值:" + s.getWarningThreshold() + s.getUnit()).collect(Collectors.joining("; "))
                + "; 【临期预警】" +
                expiringStocks.stream().map(s -> s.getName() + " 保质期至:" + s.getExpiryDate()).collect(Collectors.joining("; "));

        String systemPrompt = "你是一位食材管理专家，根据以下预警数据，分析紧急程度，给出具体的处理建议和采购优先级。";
        String aiSummary = cozeUtil.chatWithLLM(systemPrompt, warningData);

        AiAnalysisReport report = new AiAnalysisReport();
        report.setUserId(userId);
        report.setReportTitle("智能预警分析-" + LocalDate.now());
        report.setReportType("warning");
        report.setReportData(warningData);
        report.setSummary(aiSummary);
        report.setCreateTime(LocalDateTime.now());
        reportMapper.insert(report);
        return report;
    }

    public List<AiAnalysisReport> getReports(Long userId, String type) {
        return reportMapper.selectList(
                new LambdaQueryWrapper<AiAnalysisReport>()
                        .eq(AiAnalysisReport::getUserId, userId)
                        .eq(type != null, AiAnalysisReport::getReportType, type)
                        .orderByDesc(AiAnalysisReport::getCreateTime));
    }
}
