<template>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h1 class="logo">
                    <el-icon :size="40" color="#409EFF"><Platform /></el-icon>
                    AI 智能食材管理
                </h1>
                <p class="subtitle">智能采购 · 损耗分析 · 菜品生成</p>
            </div>
            <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleLogin">
                <el-form-item prop="username">
                    <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">登 录</el-button>
                </el-form-item>
            </el-form>
            <div class="login-footer">
                <span>演示账号: admin / admin123</span>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({ username: 'admin', password: 'admin123' })
const rules = {
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

onMounted(() => {
    localStorage.removeItem('token')
})

async function handleLogin() {
    const valid = await formRef.value.validate().catch(() => false)
    if (!valid) return
    loading.value = true
    try {
        await userStore.login(form)
        ElMessage.success('登录成功')
        router.push('/dashboard')
    } catch (e) {
        ElMessage.error(e.message || '登录失败')
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.login-container {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
    width: 420px;
    padding: 40px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 16px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
}
.login-header { text-align: center; margin-bottom: 30px; }
.logo { display: flex; align-items: center; justify-content: center; gap: 10px; font-size: 24px; color: #303133; }
.subtitle { color: #909399; font-size: 14px; margin-top: 8px; }
.login-btn { width: 100%; }
.login-footer { text-align: center; margin-top: 16px; color: #C0C4CC; font-size: 12px; }
</style>
