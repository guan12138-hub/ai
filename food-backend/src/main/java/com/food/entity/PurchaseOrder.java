package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("purchase_order")
public class PurchaseOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String orderNo;
    private Long supplierId;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
