<template>
    <router-view />
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { onMounted } from 'vue'

onMounted(() => {
    const token = localStorage.getItem('token')
    if (token) {
        useUserStore().setToken(token)
    }
})
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app { height: 100%; font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif; }
:root {
    --primary: #409EFF;
    --success: #67C23A;
    --warning: #E6A23C;
    --danger: #F56C6C;
    --bg-dark: #1d1e1f;
    --sidebar-bg: #304156;
}
</style>
