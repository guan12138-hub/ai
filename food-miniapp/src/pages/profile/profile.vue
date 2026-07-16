<template>
    <view class="page">
        <view class="profile-card">
            <view class="avatar">
                <text class="avatar-text">{{ (userInfo?.realName || userInfo?.username || '?').charAt(0) }}</text>
            </view>
            <text class="profile-name">{{ userInfo?.realName || userInfo?.username || '未登录' }}</text>
            <text class="profile-role">{{ userInfo?.role === 1 ? '管理员' : '普通商户' }}</text>
        </view>

        <view class="menu-list">
            <view class="menu-item" @click="navTo('/pages/stock/stock')">
                <text>🥦 食材库存</text>
                <text>></text>
            </view>
            <view class="menu-item" @click="navTo('/pages/ai-chat/ai-chat')">
                <text>💬 AI 智能问答</text>
                <text>></text>
            </view>
            <view class="menu-item" @click="navTo('/pages/ai-generate/ai-generate')">
                <text>📝 AI 内容生成</text>
                <text>></text>
            </view>
        </view>

        <button class="logout-btn" @click="handleLogout">退出登录</button>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() { return { userInfo: null } },
    onShow() {
        const token = uni.getStorageSync('token')
        if (token) this.loadUser()
    },
    methods: {
        async loadUser() {
            try { this.userInfo = await api.getMe() } catch(e) { console.error(e) }
        },
        navTo(url) { uni.navigateTo({ url }) },
        handleLogout() {
            uni.showModal({
                title: '提示',
                content: '确认退出登录？',
                success: (res) => {
                    if (res.confirm) {
                        uni.removeStorageSync('token')
                        uni.reLaunch({ url: '/pages/login/login' })
                    }
                }
            })
        }
    }
}
</script>

<style>
.page { min-height: 100vh; background: #f5f7fa; }
.profile-card { text-align: center; padding: 40px 20px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; }
.avatar { width: 70px; height: 70px; border-radius: 50%; background: rgba(255,255,255,0.3); display: flex; align-items: center; justify-content: center; margin: 0 auto 12px; }
.avatar-text { font-size: 30px; font-weight: bold; }
.profile-name { font-size: 18px; font-weight: bold; display: block; }
.profile-role { font-size: 13px; opacity: 0.8; margin-top: 4px; display: block; }
.menu-list { margin: 15px; background: #fff; border-radius: 12px; }
.menu-item { display: flex; justify-content: space-between; padding: 15px 20px; border-bottom: 1px solid #f0f0f0; font-size: 15px; color: #303133; }
.logout-btn { margin: 30px 15px; height: 44px; line-height: 44px; background: #fff; color: #F56C6C; border: 1px solid #F56C6C; border-radius: 8px; font-size: 15px; }
</style>
