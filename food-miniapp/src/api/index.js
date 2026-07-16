const BASE_URL = 'http://localhost:8080/api'

function request(url, method = 'GET', data = {}) {
    const token = uni.getStorageSync('token')
    return new Promise((resolve, reject) => {
        uni.request({
            url: BASE_URL + url,
            method,
            data,
            header: {
                'Content-Type': 'application/json',
                'token': token || ''
            },
            success: (res) => {
                if (res.data.code === 200) {
                    resolve(res.data.data)
                } else {
                    uni.showToast({ title: res.data.msg || '请求失败', icon: 'none' })
                    reject(res.data)
                }
            },
            fail: (err) => {
                uni.showToast({ title: '网络错误', icon: 'none' })
                reject(err)
            }
        })
    })
}

export default {
    // 认证
    login: (data) => request('/auth/login', 'POST', data),
    getMe: () => request('/auth/me'),

    // 库存
    getStockPage: (params) => request('/stock/page?' + Object.entries(params).map(([k,v]) => k+'='+v).join('&')),
    getWarningStock: () => request('/stock/warning'),
    addStock: (data) => request('/stock', 'POST', data),

    // AI 对话
    sendChat: (message, sessionId) => {
        const sid = sessionId || ''
        return new Promise((resolve, reject) => {
            const token = uni.getStorageSync('token')
            uni.request({
                url: BASE_URL + '/ai/chat/send?sessionId=' + sid,
                method: 'POST',
                data: message,
                header: { 'Content-Type': 'text/plain', 'token': token || '' },
                success: (res) => res.data.code === 200 ? resolve(res.data.data) : reject(res.data),
                fail: reject
            })
        })
    },

    // AI 生成
    generateRecipe: (prompt) => request('/ai/generate/recipe', 'POST', prompt),
    generateImage: (prompt) => request('/ai/generate/image', 'POST', prompt),
    getGenerateHistory: (params) => request('/ai/generate/history?' + new URLSearchParams(params).toString()),
}
