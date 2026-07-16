<template>
    <view class="login-page">
        <view class="login-box">
            <text class="login-title">AI 智能食材管理</text>
            <text class="login-sub">登录你的账号</text>
            <input v-model="username" class="login-input" placeholder="用户名" />
            <input v-model="password" class="login-input" type="password" placeholder="密码" />
            <button class="login-btn" @click="handleLogin">登 录</button>
            <text class="login-hint">演示账号: admin / admin123</text>
        </view>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() { return { username: 'admin', password: 'admin123', loading: false } },
    methods: {
        async handleLogin() {
            if (!this.username || !this.password) return uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
            this.loading = true
            try {
                const token = await api.login({ username: this.username, password: this.password })
                uni.setStorageSync('token', token)
                uni.showToast({ title: '登录成功', icon: 'success' })
                uni.reLaunch({ url: '/pages/index/index' })
            } catch(e) {
                uni.showToast({ title: e.msg || '登录失败', icon: 'none' })
            } finally { this.loading = false }
        }
    }
}
</script>

<style>
.login-page { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea, #764ba2); }
.login-box { width: 85%; background: rgba(255,255,255,0.95); border-radius: 16px; padding: 40px 30px; }
.login-title { font-size: 22px; font-weight: bold; color: #303133; text-align: center; display: block; }
.login-sub { font-size: 13px; color: #909399; text-align: center; display: block; margin: 8px 0 30px; }
.login-input { height: 48px; border: 1px solid #dcdfe6; border-radius: 8px; padding: 0 15px; font-size: 15px; margin-bottom: 15px; }
.login-btn { width: 100%; height: 48px; line-height: 48px; background: #409EFF; color: #fff; border: none; border-radius: 8px; font-size: 16px; margin-top: 10px; }
.login-hint { font-size: 12px; color: #C0C4CC; text-align: center; display: block; margin-top: 16px; }
</style>
