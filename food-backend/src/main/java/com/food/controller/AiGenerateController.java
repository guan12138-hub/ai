package com.food.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.food.entity.AiGenerateResource;
import com.food.service.ai.AiGenerateService;
import com.food.util.R;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ai/generate")
public class AiGenerateController {

    private final AiGenerateService generateService;

    public AiGenerateController(AiGenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping("/recipe")
    public R<AiGenerateResource> recipe(@RequestBody String prompt) {
        return R.ok(generateService.generateRecipe(StpUtil.getLoginIdAsLong(), prompt));
    }

    @PostMapping("/purchase-report")
    public R<AiGenerateResource> purchaseReport() {
        return R.ok(generateService.generatePurchaseReport(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/loss-report")
    public R<AiGenerateResource> lossReport(@RequestBody String lossData) {
        return R.ok(generateService.generateLossReport(StpUtil.getLoginIdAsLong(), lossData));
    }

    @PostMapping("/image")
    public R<AiGenerateResource> image(@RequestBody String prompt) {
        return R.ok(generateService.generateImage(StpUtil.getLoginIdAsLong(), prompt));
    }

    @GetMapping("/history")
    public R<List<AiGenerateResource>> history(@RequestParam(required = false) String type) {
        return R.ok(generateService.getHistory(StpUtil.getLoginIdAsLong(), type));
    }
}
