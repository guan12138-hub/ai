package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.FoodStock;
import com.food.mapper.FoodStockMapper;
import com.food.service.FoodStockService;
import org.springframework.stereotype.Service;

@Service
public class FoodStockServiceImpl extends ServiceImpl<FoodStockMapper, FoodStock> implements FoodStockService {}
