package com.food.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.entity.FoodLossData;
import com.food.mapper.FoodLossDataMapper;
import com.food.service.FoodLossDataService;
import org.springframework.stereotype.Service;

@Service
public class FoodLossDataServiceImpl extends ServiceImpl<FoodLossDataMapper, FoodLossData> implements FoodLossDataService {}
