package com.food.controller;

import com.food.util.ExcelUtil;
import com.food.util.R;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final ExcelUtil excelUtil;

    public FileUploadController(ExcelUtil excelUtil) {
        this.excelUtil = excelUtil;
    }

    @PostMapping("/excel")
    public R<List<Map<String, Object>>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            return R.ok(excelUtil.parseExcel(file));
        } catch (Exception e) {
            return R.error("Excel解析失败: " + e.getMessage());
        }
    }
}
