package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.entity.FoodLossData;
import com.food.service.FoodLossDataService;
import com.food.util.R;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loss")
public class FoodLossController {

    private final FoodLossDataService lossService;

    public FoodLossController(FoodLossDataService lossService) {
        this.lossService = lossService;
    }

    @GetMapping("/page")
    public R<Page<FoodLossData>> page(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return R.ok(lossService.page(new Page<>(page, size),
                new LambdaQueryWrapper<FoodLossData>()
                        .eq(FoodLossData::getUserId, StpUtil.getLoginIdAsLong())
                        .orderByDesc(FoodLossData::getCreateTime)));
    }

    @PostMapping
    public R<Void> add(@RequestBody FoodLossData data) {
        data.setUserId(StpUtil.getLoginIdAsLong());
        lossService.save(data);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        lossService.removeById(id);
        return R.ok();
    }
}
