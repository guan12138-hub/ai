<template>
    <view class="page">
        <view class="header-banner">
            <text class="banner-title">AI 智能食材管理</text>
            <text class="banner-sub">智能采购 · 损耗分析 · 菜品生成</text>
        </view>

        <view class="stats-grid">
            <view class="stat-item" @click="navTo('/pages/stock/stock')">
                <text class="stat-num">{{ stats.stockCount || '--' }}</text>
                <text class="stat-label">食材种类</text>
            </view>
            <view class="stat-item warning" @click="navTo('/pages/stock/stock')">
                <text class="stat-num">{{ stats.warningCount || '--' }}</text>
                <text class="stat-label">低库存预警</text>
            </view>
            <view class="stat-item" @click="navTo('/pages/ai-chat/ai-chat')">
                <text class="stat-num">AI</text>
                <text class="stat-label">智能问答</text>
            </view>
            <view class="stat-item" @click="navTo('/pages/ai-generate/ai-generate')">
                <text class="stat-num">AI</text>
                <text class="stat-label">智能生成</text>
            </view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">⚠️ 库存预警</text>
                <text class="section-more" @click="navTo('/pages/stock/stock')">查看全部 ></text>
            </view>
            <view v-for="item in warningList" :key="item.id" class="list-item">
                <view class="item-left">
                    <text class="item-name">{{ item.name }}</text>
                    <text class="item-desc">库存: {{ item.stockQuantity }}{{ item.unit }} / 阈值: {{ item.warningThreshold }}{{ item.unit }}</text>
                </view>
                <view class="item-tag danger">预警</view>
            </view>
            <view v-if="!warningList.length" class="empty">暂无预警数据</view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">🤖 AI 快捷操作</text>
            </view>
            <view class="quick-grid">
                <view class="quick-item" @click="navTo('/pages/ai-chat/ai-chat')">
                    <text class="quick-icon">💬</text>
                    <text class="quick-text">AI问答</text>
                </view>
                <view class="quick-item" @click="navTo('/pages/ai-generate/ai-generate')">
                    <text class="quick-icon">📝</text>
                    <text class="quick-text">生成菜谱</text>
                </view>
                <view class="quick-item" @click="navTo('/pages/ai-generate/ai-generate')">
                    <text class="quick-icon">🎨</text>
                    <text class="quick-text">AI绘图</text>
                </view>
                <view class="quick-item" @click="navTo('/pages/ai-generate/ai-generate')">
                    <text class="quick-icon">📊</text>
                    <text class="quick-text">采购报表</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() {
        return { stats: {}, warningList: [], token: '' }
    },
    onLoad() {
        this.token = uni.getStorageSync('token')
        if (!this.token) {
            uni.showModal({ title: '提示', content: '请先登录', success: () => uni.navigateTo({ url: '/pages/login/login' }) })
            return
        }
        this.loadData()
    },
    onPullDownRefresh() {
        this.loadData()
        uni.stopPullDownRefresh()
    },
    methods: {
        async loadData() {
            try {
                const page = await api.getStockPage({ page: 1, size: 100 })
                this.stats.stockCount = page.records?.length || 0
                const w = await api.getWarningStock()
                this.warningList = w || []
                this.stats.warningCount = w?.length || 0
            } catch(e) { console.error(e) }
        },
        navTo(url) { uni.navigateTo({ url }) }
    }
}
</script>

<style>
.page { min-height: 100vh; background: #f5f7fa; padding-bottom: 20px; }
.header-banner { background: linear-gradient(135deg, #667eea, #764ba2); padding: 40px 20px; color: #fff; }
.banner-title { font-size: 24px; font-weight: bold; display: block; }
.banner-sub { font-size: 13px; opacity: 0.8; margin-top: 6px; display: block; }
.stats-grid { display: flex; margin: -20px 15px 0; background: #fff; border-radius: 12px; padding: 15px 0; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.stat-item { flex: 1; text-align: center; }
.stat-num { font-size: 22px; font-weight: bold; color: #409EFF; display: block; }
.stat-item.warning .stat-num { color: #E6A23C; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; display: block; }
.section { margin: 15px; background: #fff; border-radius: 12px; padding: 15px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 16px; font-weight: bold; }
.section-more { font-size: 12px; color: #409EFF; }
.list-item { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.item-name { font-size: 15px; font-weight: 500; }
.item-desc { font-size: 12px; color: #909399; margin-top: 4px; }
.item-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.item-tag.danger { background: #fef0f0; color: #F56C6C; }
.empty { color: #C0C4CC; text-align: center; padding: 20px; }
.quick-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.quick-item { text-align: center; padding: 15px 0; }
.quick-icon { font-size: 28px; display: block; }
.quick-text { font-size: 12px; color: #606266; margin-top: 6px; display: block; }
</style>
