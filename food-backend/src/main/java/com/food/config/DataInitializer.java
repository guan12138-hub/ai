package com.food.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.entity.*;
import com.food.mapper.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FoodStockMapper stockMapper;
    private final SupplierMapper supplierMapper;

    public DataInitializer(FoodStockMapper stockMapper, SupplierMapper supplierMapper) {
        this.stockMapper = stockMapper;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public void run(String... args) {
        // 只在空表时插入演示数据
        if (stockMapper.selectCount(new LambdaQueryWrapper<>()) == 0) {
            insertSampleData();
        }
    }

    private void insertSampleData() {
        // 供应商
        Supplier s1 = new Supplier(); s1.setName("新鲜蔬菜批发市场"); s1.setContactPerson("张经理"); s1.setPhone("13800138001"); s1.setAddress("市中心农贸区A区"); s1.setStatus(1); supplierMapper.insert(s1);
        Supplier s2 = new Supplier(); s2.setName("放心肉业有限公司"); s2.setContactPerson("李主管"); s2.setPhone("13800138002"); s2.setAddress("食品工业园区B座"); s2.setStatus(1); supplierMapper.insert(s2);
        Supplier s3 = new Supplier(); s3.setName("海鲜水产直供"); s3.setContactPerson("王老板"); s3.setPhone("13800138003"); s3.setAddress("码头路88号"); s3.setStatus(1); supplierMapper.insert(s3);
        Supplier s4 = new Supplier(); s4.setName("粮油调味品商行"); s4.setContactPerson("陈经理"); s4.setPhone("13800138004"); s4.setAddress("粮油市场2排3号"); s4.setStatus(1); supplierMapper.insert(s4);

        // 食材库存
        insertStock("大白菜", "蔬菜", 50, "斤", 2.5, s1.getId(), LocalDate.of(2026, 7, 20), 10);
        insertStock("番茄", "蔬菜", 30, "斤", 4.0, s1.getId(), LocalDate.of(2026, 7, 18), 8);
        insertStock("青椒", "蔬菜", 25, "斤", 3.5, s1.getId(), LocalDate.of(2026, 7, 19), 8);
        insertStock("黄瓜", "蔬菜", 20, "斤", 3.0, s1.getId(), LocalDate.of(2026, 7, 18), 5);
        insertStock("猪肉(五花)", "肉类", 15, "斤", 18.0, s2.getId(), LocalDate.of(2026, 7, 25), 5);
        insertStock("鸡胸肉", "肉类", 10, "斤", 12.0, s2.getId(), LocalDate.of(2026, 7, 24), 5);
        insertStock("草鱼", "水产", 8, "斤", 9.0, s3.getId(), LocalDate.of(2026, 7, 19), 3);
        insertStock("基围虾", "水产", 5, "斤", 35.0, s3.getId(), LocalDate.of(2026, 7, 18), 3);
        insertStock("大米", "粮油", 100, "斤", 2.8, s4.getId(), LocalDate.of(2027, 1, 15), 20);
        insertStock("食用油", "粮油", 10, "桶", 55.0, s4.getId(), LocalDate.of(2026, 12, 1), 3);
        insertStock("酱油", "调味品", 12, "瓶", 8.0, s4.getId(), LocalDate.of(2026, 10, 1), 5);
        insertStock("食用盐", "调味品", 20, "袋", 2.0, s4.getId(), LocalDate.of(2027, 6, 1), 5);
        // 临期预警数据
        insertStock("豆腐", "其他", 3, "斤", 2.0, null, LocalDate.of(2026, 7, 17), 10);
        insertStock("鲜牛奶", "其他", 2, "瓶", 6.0, null, LocalDate.of(2026, 7, 17), 5);
    }

    private void insertStock(String name, String category, double qty, String unit, double price, Long supplierId, LocalDate expiry, double threshold) {
        FoodStock stock = new FoodStock();
        stock.setUserId(1L);
        stock.setName(name);
        stock.setCategory(category);
        stock.setStockQuantity(BigDecimal.valueOf(qty));
        stock.setUnit(unit);
        stock.setPurchasePrice(BigDecimal.valueOf(price));
        stock.setSupplierId(supplierId);
        stock.setExpiryDate(expiry);
        stock.setWarningThreshold(BigDecimal.valueOf(threshold));
        stock.setStorageLocation("主仓库");
        stock.setStatus(1);
        stockMapper.insert(stock);
    }
}
