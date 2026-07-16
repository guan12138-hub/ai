import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
    baseURL: '/api',
    timeout: 60000
})

request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.token = token
    }
    return config
})

request.interceptors.response.use(
    response => {
        const res = response.data
        if (res.code !== 200) {
            // Token expired or auth error - redirect to login
            if (res.msg && (res.msg.includes('token') || res.msg.includes('未登录') || res.msg.includes('登录'))) {
                localStorage.removeItem('token')
                ElMessage.error('登录已过期，请重新登录')
                router.push('/login')
                return Promise.reject(new Error(res.msg))
            }
            ElMessage.error(res.msg || '请求失败')
            return Promise.reject(new Error(res.msg))
        }
        return res.data
    },
    error => {
        if (error.response?.status === 401 || error.message?.includes('token')) {
            localStorage.removeItem('token')
            router.push('/login')
            return Promise.reject(error)
        }
        ElMessage.error(error.message || '网络错误')
        return Promise.reject(error)
    }
)

export default request
