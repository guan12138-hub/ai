package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("food_loss_data")
public class FoodLossData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long foodId;
    private String foodName;
    private BigDecimal lossQuantity;
    private String unit;
    private String lossReason;
    private BigDecimal lossAmount;
    private LocalDate recordDate;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
}
