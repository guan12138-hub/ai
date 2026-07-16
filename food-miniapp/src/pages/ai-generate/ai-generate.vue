<template>
    <view class="page">
        <view class="section">
            <view class="section-header">
                <text class="section-title">📝 AI 内容生成</text>
            </view>
            <view class="tab-bar">
                <view :class="'tab ' + (activeTab === 'recipe' ? 'active' : '')" @click="activeTab='recipe'">生成菜谱</view>
                <view :class="'tab ' + (activeTab === 'image' ? 'active' : '')" @click="activeTab='image'">AI 绘图</view>
            </view>

            <view v-if="activeTab === 'recipe'">
                <textarea v-model="recipePrompt" class="textarea" placeholder="请输入食材清单，如：鸡蛋3个、番茄2个、葱适量" />
                <button class="action-btn" @click="genRecipe" :disabled="recipeLoading">{{ recipeLoading ? '生成中...' : 'AI 生成菜谱' }}</button>
                <view v-if="recipeResult" class="result">{{ recipeResult }}</view>
            </view>

            <view v-if="activeTab === 'image'">
                <textarea v-model="imagePrompt" class="textarea" placeholder="输入图片描述词，如：食堂清淡菜品海报" />
                <button class="action-btn" @click="genImage" :disabled="imageLoading">{{ imageLoading ? '生成中...' : '生成图片' }}</button>
                <image v-if="imageUrl" :src="imageUrl" mode="widthFix" style="width:100%;margin-top:12px;border-radius:8px;" />
            </view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">📚 生成历史</text>
            </view>
            <view v-for="item in history" :key="item.id" class="history-item">
                <text class="history-title">{{ item.title }}</text>
                <text class="history-time">{{ item.createTime?.substring(0,10) }}</text>
            </view>
            <view v-if="!history.length" class="empty">暂无记录</view>
        </view>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() {
        return {
            activeTab: 'recipe',
            recipePrompt: '', recipeResult: '', recipeLoading: false,
            imagePrompt: '', imageUrl: '', imageLoading: false,
            history: []
        }
    },
    onShow() { this.loadHistory() },
    methods: {
        async genRecipe() {
            if (!this.recipePrompt.trim()) return uni.showToast({ title: '请输入食材清单', icon: 'none' })
            this.recipeLoading = true; this.recipeResult = ''
            try { const res = await api.generateRecipe(this.recipePrompt); this.recipeResult = res.content } catch(e) { uni.showToast({ title: '生成失败', icon: 'none' }) }
            finally { this.recipeLoading = false; this.loadHistory() }
        },
        async genImage() {
            if (!this.imagePrompt.trim()) return uni.showToast({ title: '请输入描述词', icon: 'none' })
            this.imageLoading = true; this.imageUrl = ''
            try { const res = await api.generateImage(this.imagePrompt); this.imageUrl = res.imageUrl } catch(e) { uni.showToast({ title: '生成失败', icon: 'none' }) }
            finally { this.imageLoading = false; this.loadHistory() }
        },
        async loadHistory() {
            try { this.history = await api.getGenerateHistory({}) } catch(e) { /* ignore */ }
        }
    }
}
</script>

<style>
.page { min-height: 100vh; background: #f5f7fa; padding-bottom: 20px; }
.section { margin: 15px; background: #fff; border-radius: 12px; padding: 15px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 16px; font-weight: bold; }
.tab-bar { display: flex; margin-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
.tab { padding: 8px 20px; font-size: 14px; color: #909399; }
.tab.active { color: #409EFF; border-bottom: 2px solid #409EFF; }
.textarea { width: 100%; min-height: 100px; border: 1px solid #dcdfe6; border-radius: 8px; padding: 10px; font-size: 14px; }
.action-btn { width: 100%; margin-top: 12px; height: 44px; background: #409EFF; color: #fff; border: none; border-radius: 8px; font-size: 15px; }
.result { margin-top: 12px; padding: 12px; background: #f5f7fa; border-radius: 8px; font-size: 13px; line-height: 1.8; }
.history-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.history-title { font-size: 14px; }
.history-time { font-size: 12px; color: #C0C4CC; }
.empty { color: #C0C4CC; text-align: center; padding: 20px; }
</style>
