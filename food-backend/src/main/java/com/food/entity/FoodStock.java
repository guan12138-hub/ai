package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("food_stock")
public class FoodStock {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String category;
    private BigDecimal stockQuantity;
    private String unit;
    private BigDecimal purchasePrice;
    private Long supplierId;
    private LocalDate expiryDate;
    private BigDecimal warningThreshold;
    private String storageLocation;
    private String remark;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
