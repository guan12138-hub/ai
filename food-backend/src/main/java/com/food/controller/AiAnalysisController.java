package com.food.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.food.entity.AiAnalysisReport;
import com.food.service.ai.AiAnalysisService;
import com.food.util.R;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/analysis")
public class AiAnalysisController {

    private final AiAnalysisService analysisService;

    public AiAnalysisController(AiAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/loss-trend")
    public R<AiAnalysisReport> lossTrend(@RequestBody Map<String, String> params) {
        return R.ok(analysisService.analyzeLossTrend(
                StpUtil.getLoginIdAsLong(),
                params.get("startDate"),
                params.get("endDate")));
    }

    @PostMapping("/cost")
    public R<AiAnalysisReport> cost() {
        return R.ok(analysisService.analyzeCost(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/warning")
    public R<AiAnalysisReport> warning() {
        return R.ok(analysisService.analyzeWarning(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/reports")
    public R<List<AiAnalysisReport>> reports(@RequestParam(required = false) String type) {
        return R.ok(analysisService.getReports(StpUtil.getLoginIdAsLong(), type));
    }
}
