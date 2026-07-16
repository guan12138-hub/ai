import request from './request'

// 认证
export const login = (data) => request.post('/auth/login', data)
export const getCurrentUser = () => request.get('/auth/me')
export const register = (data) => request.post('/auth/register', data)

// 食材库存
export const getStockPage = (params) => request.get('/stock/page', { params })
export const getStock = (id) => request.get('/stock/' + id)
export const addStock = (data) => request.post('/stock', data)
export const updateStock = (data) => request.put('/stock', data)
export const deleteStock = (id) => request.delete('/stock/' + id)
export const getWarningStock = () => request.get('/stock/warning')
export const getExpiringStock = () => request.get('/stock/expiring')

// 采购订单
export const getOrderPage = (params) => request.get('/order/page', { params })
export const addOrder = (data) => request.post('/order', data)
export const updateOrderStatus = (params) => request.put('/order/status', null, { params })
export const getOrderItems = (orderId) => request.get('/order/items/' + orderId)

// 供应商
export const getSupplierList = () => request.get('/supplier/list')
export const addSupplier = (data) => request.post('/supplier', data)
export const updateSupplier = (data) => request.put('/supplier', data)
export const deleteSupplier = (id) => request.delete('/supplier/' + id)

// 损耗
export const getLossPage = (params) => request.get('/loss/page', { params })
export const addLoss = (data) => request.post('/loss', data)
export const deleteLoss = (id) => request.delete('/loss/' + id)

// AI 对话
export const sendChat = (message, sessionId) => request.post('/ai/chat/send?sessionId=' + (sessionId || ''), message, {
    headers: { 'Content-Type': 'text/plain' }
})
export const getChatHistory = (sessionId) => request.get('/ai/chat/history', { params: { sessionId } })

// AI 生成
export const generateRecipe = (prompt) => request.post('/ai/generate/recipe', prompt, { headers: { 'Content-Type': 'text/plain' } })
export const generatePurchaseReport = () => request.post('/ai/generate/purchase-report')
export const generateLossReport = (data) => request.post('/ai/generate/loss-report', data, { headers: { 'Content-Type': 'text/plain' } })
export const generateImage = (prompt) => request.post('/ai/generate/image', prompt, { headers: { 'Content-Type': 'text/plain' } })
export const getGenerateHistory = (params) => request.get('/ai/generate/history', { params })

// AI 分析
export const analyzeLossTrend = (data) => request.post('/ai/analysis/loss-trend', data)
export const analyzeCost = () => request.post('/ai/analysis/cost')
export const analyzeWarning = () => request.post('/ai/analysis/warning')
export const getAnalysisReports = (params) => request.get('/ai/analysis/reports', { params })

// 上传
export const uploadExcel = (file) => {
    const form = new FormData()
    form.append('file', file)
    return request.post('/upload/excel', form)
}
