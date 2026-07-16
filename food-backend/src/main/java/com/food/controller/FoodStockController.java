package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.entity.FoodStock;
import com.food.mapper.FoodStockMapper;
import com.food.service.FoodStockService;
import com.food.util.R;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class FoodStockController {

    private final FoodStockService stockService;
    private final FoodStockMapper stockMapper;

    public FoodStockController(FoodStockService stockService, FoodStockMapper stockMapper) {
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    @GetMapping("/page")
    public R<Page<FoodStock>> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String category) {
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<FoodStock> wrapper = new LambdaQueryWrapper<FoodStock>()
                .eq(FoodStock::getUserId, userId)
                .like(keyword != null, FoodStock::getName, keyword)
                .eq(category != null, FoodStock::getCategory, category)
                .orderByDesc(FoodStock::getCreateTime);
        return R.ok(stockService.page(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public R<FoodStock> get(@PathVariable Long id) {
        return R.ok(stockService.getById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody FoodStock stock) {
        stock.setUserId(StpUtil.getLoginIdAsLong());
        stockService.save(stock);
        return R.ok();
    }

    @PutMapping
    public R<Void> update(@RequestBody FoodStock stock) {
        stockService.updateById(stock);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        stockService.removeById(id);
        return R.ok();
    }

    @GetMapping("/warning")
    public R<List<FoodStock>> warning() {
        return R.ok(stockMapper.selectWarningStock(StpUtil.getLoginIdAsLong()));
    }

    @GetMapping("/expiring")
    public R<List<FoodStock>> expiring() {
        return R.ok(stockMapper.selectExpiringSoon(StpUtil.getLoginIdAsLong()));
    }
}
