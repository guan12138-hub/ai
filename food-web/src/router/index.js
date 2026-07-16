import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue'),
        meta: { noAuth: true }
    },
    {
        path: '/',
        component: () => import('@/views/Layout.vue'),
        redirect: '/dashboard',
        children: [
            { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue') },
            { path: 'stock', name: 'Stock', component: () => import('@/views/Stock.vue') },
            { path: 'order', name: 'Order', component: () => import('@/views/Order.vue') },
            { path: 'supplier', name: 'Supplier', component: () => import('@/views/Supplier.vue') },
            { path: 'loss', name: 'Loss', component: () => import('@/views/Loss.vue') },
            { path: 'ai-chat', name: 'AiChat', component: () => import('@/views/AiChat.vue') },
            { path: 'ai-generate', name: 'AiGenerate', component: () => import('@/views/AiGenerate.vue') },
            { path: 'ai-analysis', name: 'AiAnalysis', component: () => import('@/views/AiAnalysis.vue') },
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (!to.meta.noAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router
