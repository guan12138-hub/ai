package com.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ai_analysis_report")
public class AiAnalysisReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String reportTitle;
    private String reportType;
    private String reportData;
    private String summary;
    private String chartConfig;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime createTime;
}
