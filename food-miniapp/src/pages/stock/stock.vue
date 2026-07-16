<template>
    <view class="page">
        <view class="section">
            <view class="section-header">
                <text class="section-title">🥦 食材库存</text>
                <text class="section-more" @click="showAdd = true">+ 新增</text>
            </view>
            <view v-for="item in stockList" :key="item.id" class="stock-item">
                <view class="stock-info">
                    <text class="stock-name">{{ item.name }}</text>
                    <text class="stock-cate">{{ item.category }}</text>
                </view>
                <view class="stock-right">
                    <text class="stock-qty">{{ item.stockQuantity }}{{ item.unit }}</text>
                    <view :class="'stock-tag ' + (item.stockQuantity <= item.warningThreshold ? 'danger' : 'normal')">
                        {{ item.stockQuantity <= item.warningThreshold ? '预警' : '正常' }}
                    </view>
                </view>
            </view>
            <view v-if="!stockList.length" class="empty">暂无库存数据</view>
        </view>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() {
        return { stockList: [], showAdd: false }
    },
    onShow() { this.loadData() },
    methods: {
        async loadData() {
            try { const page = await api.getStockPage({ page: 1, size: 50 }); this.stockList = page.records || [] }
            catch(e) { console.error(e) }
        }
    }
}
</script>

<style>
.page { min-height: 100vh; background: #f5f7fa; }
.section { margin: 15px; background: #fff; border-radius: 12px; padding: 15px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 16px; font-weight: bold; }
.section-more { font-size: 14px; color: #409EFF; }
.stock-item { display: flex; justify-content: space-between; align-items: center; padding: 14px 0; border-bottom: 1px solid #f0f0f0; }
.stock-info { display: flex; flex-direction: column; }
.stock-name { font-size: 15px; font-weight: 500; }
.stock-cate { font-size: 12px; color: #909399; margin-top: 2px; }
.stock-right { display: flex; align-items: center; gap: 10px; }
.stock-qty { font-size: 16px; font-weight: bold; color: #303133; }
.stock-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.stock-tag.normal { background: #f0f9eb; color: #67C23A; }
.stock-tag.danger { background: #fef0f0; color: #F56C6C; }
.empty { color: #C0C4CC; text-align: center; padding: 30px; }
</style>
