package com.food.dto;

import lombok.Data;
import java.util.List;

@Data
public class StockAnalysisDTO {
    private String periodStart;
    private String periodEnd;
    private List<Long> foodIds;
}
