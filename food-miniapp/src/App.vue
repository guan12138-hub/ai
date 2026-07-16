<template>
    <page-meta>
        <navigation-bar :title="navTitle" :front-color="'#ffffff'" :background-color="'#409EFF'" />
    </page-meta>
    <view class="app-container">
        <uni-page ref="page">
            <uni-page-head />
            <uni-page-body>
                <slot />
            </uni-page-body>
        </uni-page>
    </view>
</template>

<script>
export default {
    computed: {
        navTitle() {
            const pages = getCurrentPages()
            const page = pages[pages.length - 1]
            const route = page?.route || ''
            const map = {
                'pages/index/index': '首页',
                'pages/stock/stock': '食材库存',
                'pages/ai-chat/ai-chat': 'AI 问答',
                'pages/ai-generate/ai-generate': 'AI 生成',
                'pages/profile/profile': '我的',
            }
            return map[route] || 'AI智能食材管理'
        }
    }
}
</script>

<style>
.app-container { min-height: 100vh; background: #f5f7fa; }
</style>
