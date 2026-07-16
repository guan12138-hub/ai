<template>
    <div class="stock-page">
        <el-card shadow="hover">
            <template #header>
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <span style="font-weight:bold;">🥦 食材库存管理</span>
                    <div>
                        <el-input v-model="keyword" placeholder="搜索食材" clearable style="width:200px;margin-right:10px;" @clear="fetchData" @keyup.enter="fetchData" />
                        <el-select v-model="category" placeholder="分类" clearable style="width:120px;margin-right:10px;" @change="fetchData">
                            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
                        </el-select>
                        <el-button type="primary" @click="showDialog = true">+ 新增食材</el-button>
                    </div>
                </div>
            </template>
            <el-table :data="tableData" stripe v-loading="loading">
                <el-table-column prop="name" label="食材名称" min-width="120" />
                <el-table-column prop="category" label="分类" width="80">
                    <template #default="{row}"><el-tag>{{ row.category }}</el-tag></template>
                </el-table-column>
                <el-table-column prop="stockQuantity" label="库存数量" width="100" />
                <el-table-column prop="unit" label="单位" width="60" />
                <el-table-column prop="purchasePrice" label="单价(元)" width="90" />
                <el-table-column prop="expiryDate" label="保质期" width="110" />
                <el-table-column prop="warningThreshold" label="预警阈值" width="100" />
                <el-table-column label="状态" width="80">
                    <template #default="{row}">
                        <el-tag :type="row.stockQuantity <= row.warningThreshold ? 'danger' : 'success'" size="small">
                            {{ row.stockQuantity <= row.warningThreshold ? '预警' : '正常' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="160" fixed="right">
                    <template #default="{row}">
                        <el-button size="small" @click="edit(row)">编辑</el-button>
                        <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)">
                            <template #reference><el-button size="small" type="danger">删除</el-button></template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <div style="margin-top:16px;text-align:right;">
                <el-pagination v-model:current="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="fetchData" />
            </div>
        </el-card>

        <el-dialog v-model="showDialog" :title="isEdit ? '编辑食材' : '新增食材'" width="500px">
            <el-form :model="form" label-width="90px">
                <el-form-item label="食材名称"><el-input v-model="form.name" /></el-form-item>
                <el-form-item label="分类">
                    <el-select v-model="form.category">
                        <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
                    </el-select>
                </el-form-item>
                <el-form-item label="库存数量"><el-input-number v-model="form.stockQuantity" :min="0" /></el-form-item>
                <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
                <el-form-item label="采购单价"><el-input-number v-model="form.purchasePrice" :min="0" :precision="2" /></el-form-item>
                <el-form-item label="保质期"><el-date-picker v-model="form.expiryDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
                <el-form-item label="预警阈值"><el-input-number v-model="form.warningThreshold" :min="0" /></el-form-item>
                <el-form-item label="存放位置"><el-input v-model="form.storageLocation" /></el-form-item>
                <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="showDialog = false">取消</el-button>
                <el-button type="primary" @click="handleSave">保存</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getStockPage, addStock, updateStock, deleteStock } from '@/api'

const categories = ['蔬菜', '肉类', '水产', '调味品', '粮油', '其他']
const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(20)
const total = ref(0)
const keyword = ref('')
const category = ref('')
const showDialog = ref(false)
const isEdit = ref(false)

const form = reactive({
    id: null, name: '', category: '蔬菜', stockQuantity: 0, unit: '斤',
    purchasePrice: null, expiryDate: null, warningThreshold: 10,
    storageLocation: '', remark: ''
})

async function fetchData() {
    loading.value = true
    try {
        const res = await getStockPage({ page: page.value, size: size.value, keyword: keyword.value || undefined, category: category.value || undefined })
        tableData.value = res.records
        total.value = res.total
    } finally { loading.value = false }
}

function edit(row) {
    isEdit.value = true
    Object.assign(form, row)
    showDialog.value = true
}

async function handleSave() {
    if (isEdit.value) {
        await updateStock(form)
        ElMessage.success('更新成功')
    } else {
        await addStock(form)
        ElMessage.success('新增成功')
    }
    showDialog.value = false
    resetForm()
    fetchData()
}

async function handleDelete(id) {
    await deleteStock(id)
    ElMessage.success('删除成功')
    fetchData()
}

function resetForm() {
    isEdit.value = false
    form.id = null; form.name = ''; form.category = '蔬菜'; form.stockQuantity = 0
    form.unit = '斤'; form.purchasePrice = null; form.expiryDate = null
    form.warningThreshold = 10; form.storageLocation = ''; form.remark = ''
}

onMounted(fetchData)
</script>
