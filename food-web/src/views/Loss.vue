<template>
    <el-card shadow="hover">
        <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <span style="font-weight:bold;">📉 食材损耗记录</span>
                <el-button type="primary" @click="showDialog = true">+ 新增损耗</el-button>
            </div>
        </template>
        <el-table :data="lossList" stripe v-loading="loading">
            <el-table-column prop="foodName" label="食材名称" width="120" />
            <el-table-column prop="lossQuantity" label="损耗数量" width="100" />
            <el-table-column prop="unit" label="单位" width="60" />
            <el-table-column prop="lossReason" label="损耗原因" width="100">
                <template #default="{row}"><el-tag>{{ row.lossReason }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="lossAmount" label="损耗金额(元)" width="110" />
            <el-table-column prop="recordDate" label="记录日期" width="110" />
            <el-table-column prop="operator" label="操作人" width="80" />
            <el-table-column label="操作" width="80">
                <template #default="{row}">
                    <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)"><template #reference><el-button size="small" type="danger">删除</el-button></template></el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="margin-top:16px;text-align:right;">
            <el-pagination v-model:current="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="fetchData" />
        </div>
    </el-card>

    <el-dialog v-model="showDialog" title="新增损耗记录" width="400px">
        <el-form :model="form" label-width="90px">
            <el-form-item label="食材名称"><el-input v-model="form.foodName" /></el-form-item>
            <el-form-item label="损耗数量"><el-input-number v-model="form.lossQuantity" :min="0" /></el-form-item>
            <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
            <el-form-item label="损耗原因">
                <el-select v-model="form.lossReason"><el-option v-for="r in ['过期','破损','变质','其他']" :key="r" :label="r" :value="r" /></el-select>
            </el-form-item>
            <el-form-item label="损耗金额"><el-input-number v-model="form.lossAmount" :min="0" :precision="2" /></el-form-item>
            <el-form-item label="记录日期"><el-date-picker v-model="form.recordDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
            <el-form-item label="操作人"><el-input v-model="form.operator" /></el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="showDialog = false">取消</el-button>
            <el-button type="primary" @click="handleSave">保存</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getLossPage, addLoss, deleteLoss } from '@/api'

const lossList = ref([])
const loading = ref(false)
const page = ref(1), size = ref(20), total = ref(0)
const showDialog = ref(false)
const form = reactive({ foodName: '', lossQuantity: 0, unit: '斤', lossReason: '过期', lossAmount: 0, recordDate: '', operator: '' })

async function fetchData() {
    loading.value = true
    try { const res = await getLossPage({ page: page.value, size: size.value }); lossList.value = res.records; total.value = res.total } finally { loading.value = false }
}

async function handleSave() { await addLoss(form); ElMessage.success('新增成功'); showDialog.value = false; fetchData() }
async function handleDelete(id) { await deleteLoss(id); ElMessage.success('已删除'); fetchData() }

onMounted(fetchData)
</script>
