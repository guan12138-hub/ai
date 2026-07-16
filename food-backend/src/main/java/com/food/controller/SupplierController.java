package com.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.entity.Supplier;
import com.food.service.SupplierService;
import com.food.util.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/list")
    public R<List<Supplier>> list() {
        return R.ok(supplierService.list(new LambdaQueryWrapper<Supplier>()
                .eq(Supplier::getStatus, 1)));
    }

    @PostMapping
    public R<Void> add(@RequestBody Supplier supplier) {
        supplierService.save(supplier);
        return R.ok();
    }

    @PutMapping
    public R<Void> update(@RequestBody Supplier supplier) {
        supplierService.updateById(supplier);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        supplierService.removeById(id);
        return R.ok();
    }
}
