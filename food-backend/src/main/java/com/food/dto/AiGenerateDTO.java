package com.food.dto;

import lombok.Data;

@Data
public class AiGenerateDTO {
    private String type;      // recipe/poster/report/analysis
    private String prompt;
    private String extraData; // JSON 额外数据
}
