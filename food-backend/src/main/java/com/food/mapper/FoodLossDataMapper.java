package com.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food.entity.FoodLossData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface FoodLossDataMapper extends BaseMapper<FoodLossData> {
    @Select("SELECT DATE_FORMAT(record_date, '%Y-%m') as month, SUM(loss_quantity) as total_loss, SUM(loss_amount) as total_amount " +
            "FROM food_loss_data WHERE user_id = #{userId} AND record_date >= #{startDate} AND record_date <= #{endDate} " +
            "GROUP BY DATE_FORMAT(record_date, '%Y-%m') ORDER BY month")
    List<Map<String, Object>> selectLossTrend(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
