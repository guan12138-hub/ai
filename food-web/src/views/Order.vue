<template>
    <el-card shadow="hover">
        <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <span style="font-weight:bold;">📦 采购订单</span>
                <el-button type="primary" @click="showAdd = true">+ 新建订单</el-button>
            </div>
        </template>
        <el-table :data="orders" stripe v-loading="loading">
            <el-table-column prop="orderNo" label="订单编号" min-width="160" />
            <el-table-column prop="totalAmount" label="总金额(元)" width="110" />
            <el-table-column prop="orderDate" label="下单日期" width="110" />
            <el-table-column prop="deliveryDate" label="到货日期" width="110" />
            <el-table-column label="状态" width="100">
                <template #default="{row}">
                    <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="160">
                <template #default="{row}">
                    <el-button size="small" @click="viewItems(row)">详情</el-button>
                    <el-button v-if="row.status === 0" size="small" type="success" @click="updateStatus(row.id, 1)">通过</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div style="margin-top:16px;text-align:right;">
            <el-pagination v-model:current="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="fetchData" />
        </div>
    </el-card>

    <el-dialog v-model="showAdd" title="新建采购订单" width="400px">
        <el-form :model="orderForm" label-width="90px">
            <el-form-item label="订单编号"><el-input v-model="orderForm.orderNo" /></el-form-item>
            <el-form-item label="总金额"><el-input-number v-model="orderForm.totalAmount" :min="0" :precision="2" /></el-form-item>
            <el-form-item label="下单日期"><el-date-picker v-model="orderForm.orderDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
            <el-form-item label="到货日期"><el-date-picker v-model="orderForm.deliveryDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="showAdd = false">取消</el-button>
            <el-button type="primary" @click="handleAdd">保存</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderPage, addOrder, updateOrderStatus } from '@/api'

const orders = ref([])
const loading = ref(false)
const page = ref(1), size = ref(20), total = ref(0)
const showAdd = ref(false)
const orderForm = ref({ orderNo: '', totalAmount: 0, orderDate: '', deliveryDate: '' })
const statusMap = { 0: { label: '待审核', type: 'info' }, 1: { label: '已通过', type: 'success' }, 2: { label: '已到货', type: 'primary' }, 3: { label: '已取消', type: 'danger' } }

async function fetchData() {
    loading.value = true
    try {
        const res = await getOrderPage({ page: page.value, size: size.value })
        orders.value = res.records; total.value = res.total
    } finally { loading.value = false }
}

async function handleAdd() {
    await addOrder(orderForm.value)
    ElMessage.success('创建成功')
    showAdd.value = false; fetchData()
}

async function updateStatus(id, status) {
    await updateOrderStatus({ id, status })
    ElMessage.success('操作成功'); fetchData()
}

function viewItems(row) {
    ElMessage.info('订单 #' + row.orderNo + ' 状态: ' + (statusMap[row.status]?.label || '未知'))
}

onMounted(fetchData)
</script>
