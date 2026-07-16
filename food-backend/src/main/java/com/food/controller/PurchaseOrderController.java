package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food.entity.PurchaseOrder;
import com.food.entity.PurchaseOrderItem;
import com.food.mapper.PurchaseOrderItemMapper;
import com.food.service.PurchaseOrderService;
import com.food.util.R;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class PurchaseOrderController {

    private final PurchaseOrderService orderService;
    private final PurchaseOrderItemMapper orderItemMapper;

    public PurchaseOrderController(PurchaseOrderService orderService, PurchaseOrderItemMapper orderItemMapper) {
        this.orderService = orderService;
        this.orderItemMapper = orderItemMapper;
    }

    @GetMapping("/page")
    public R<Page<PurchaseOrder>> page(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        return R.ok(orderService.page(new Page<>(page, size),
                new LambdaQueryWrapper<PurchaseOrder>()
                        .eq(PurchaseOrder::getUserId, StpUtil.getLoginIdAsLong())
                        .orderByDesc(PurchaseOrder::getCreateTime)));
    }

    @GetMapping("/items/{orderId}")
    public R<List<PurchaseOrderItem>> items(@PathVariable Long orderId) {
        return R.ok(orderItemMapper.selectList(
                new LambdaQueryWrapper<PurchaseOrderItem>().eq(PurchaseOrderItem::getOrderId, orderId)));
    }

    @PostMapping
    public R<Void> add(@RequestBody PurchaseOrder order) {
        order.setUserId(StpUtil.getLoginIdAsLong());
        orderService.save(order);
        return R.ok();
    }

    @PutMapping("/status")
    public R<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        PurchaseOrder order = orderService.getById(id);
        order.setStatus(status);
        orderService.updateById(order);
        return R.ok();
    }
}
