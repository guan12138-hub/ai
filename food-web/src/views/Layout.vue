<template>
    <el-container class="layout">
        <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
            <div class="sidebar-header" @click="isCollapse = !isCollapse">
                <el-icon :size="28" color="#409EFF"><Platform /></el-icon>
                <span v-show="!isCollapse" class="sidebar-title">食材管理系统</span>
            </div>
            <el-menu :default-active="route.path" router :collapse="isCollapse" background-color="#304156"
                     text-color="#bfcbd9" active-text-color="#409EFF">
                <el-menu-item index="/dashboard">
                    <el-icon><DataAnalysis /></el-icon><span>工作台</span>
                </el-menu-item>
                <el-menu-item index="/stock">
                    <el-icon><Goods /></el-icon><span>食材库存</span>
                </el-menu-item>
                <el-menu-item index="/order">
                    <el-icon><List /></el-icon><span>采购订单</span>
                </el-menu-item>
                <el-menu-item index="/supplier">
                    <el-icon><UserFilled /></el-icon><span>供应商</span>
                </el-menu-item>
                <el-menu-item index="/loss">
                    <el-icon><Remove /></el-icon><span>损耗记录</span>
                </el-menu-item>
                <el-sub-menu index="/ai">
                    <template #title><el-icon><MagicStick /></el-icon><span>AI 智能</span></template>
                    <el-menu-item index="/ai-chat"><el-icon><ChatDotRound /></el-icon>AI 问答</el-menu-item>
                    <el-menu-item index="/ai-generate"><el-icon><Document /></el-icon>AI 生成</el-menu-item>
                    <el-menu-item index="/ai-analysis"><el-icon><TrendCharts /></el-icon>AI 分析</el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header class="header">
                <div class="header-left">
                    <el-icon @click="isCollapse = !isCollapse" style="cursor:pointer; font-size: 20px;">
                        <Fold v-if="!isCollapse" /><Expand v-else />
                    </el-icon>
                    <el-breadcrumb separator="/" style="margin-left: 16px;">
                        <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item v-if="route.meta.title">{{ route.meta.title }}</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <div class="header-right">
                    <el-dropdown @command="handleCommand">
                        <span class="user-info">
                            <el-avatar :size="32" icon="UserFilled" />
                            <span style="margin-left:8px;">{{ userStore.user?.realName || userStore.user?.username }}</span>
                            <el-icon><ArrowDown /></el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-main class="main">
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)

function handleCommand(cmd) {
    if (cmd === 'logout') {
        ElMessageBox.confirm('确认退出登录？').then(() => {
            userStore.logout()
            router.push('/login')
        }).catch(() => {})
    }
}
</script>

<style scoped>
.layout { height: 100vh; }
.sidebar { background-color: #304156; transition: width 0.3s; overflow: hidden; }
.sidebar-header { height: 60px; display: flex; align-items: center; justify-content: center; gap: 8px; cursor: pointer; }
.sidebar-title { color: #fff; font-size: 16px; font-weight: bold; white-space: nowrap; }
.header { background: #fff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; border-bottom: 1px solid #e6e6e6; box-shadow: 0 1px 4px rgba(0,0,0,0.05); }
.header-left, .header-right { display: flex; align-items: center; }
.user-info { display: flex; align-items: center; cursor: pointer; }
.main { background: #f0f2f5; padding: 20px; overflow-y: auto; }
.el-menu { border-right: none; }
</style>
