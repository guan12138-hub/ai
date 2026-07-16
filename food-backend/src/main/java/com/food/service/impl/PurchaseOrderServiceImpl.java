package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.PurchaseOrder;
import com.food.mapper.PurchaseOrderMapper;
import com.food.service.PurchaseOrderService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {}
