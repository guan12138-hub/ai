package com.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.entity.FoodStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface FoodStockMapper extends BaseMapper<FoodStock> {
    @Select("SELECT * FROM food_stock WHERE user_id = #{userId} AND stock_quantity <= warning_threshold AND status = 1")
    List<FoodStock> selectWarningStock(@Param("userId") Long userId);

    @Select("SELECT * FROM food_stock WHERE user_id = #{userId} AND expiry_date IS NOT NULL AND expiry_date <= CURDATE() + INTERVAL 3 DAY AND status = 1")
    List<FoodStock> selectExpiringSoon(@Param("userId") Long userId);
}
