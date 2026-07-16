<template>
    <el-card shadow="hover">
        <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <span style="font-weight:bold;">🏭 供应商管理</span>
                <el-button type="primary" @click="showDialog = true">+ 新增供应商</el-button>
            </div>
        </template>
        <el-table :data="suppliers" stripe>
            <el-table-column prop="name" label="名称" min-width="140" />
            <el-table-column prop="contactPerson" label="联系人" width="100" />
            <el-table-column prop="phone" label="电话" width="130" />
            <el-table-column prop="address" label="地址" min-width="160" />
            <el-table-column label="操作" width="130">
                <template #default="{row}">
                    <el-button size="small" @click="edit(row)">编辑</el-button>
                    <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)"><template #reference><el-button size="small" type="danger">删除</el-button></template></el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
    </el-card>

    <el-dialog v-model="showDialog" :title="isEdit ? '编辑供应商' : '新增供应商'" width="450px">
        <el-form :model="form" label-width="80px">
            <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
            <el-form-item label="联系人"><el-input v-model="form.contactPerson" /></el-form-item>
            <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
            <el-form-item label="地址"><el-input v-model="form.address" type="textarea" /></el-form-item>
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
import { getSupplierList, addSupplier, updateSupplier, deleteSupplier } from '@/api'

const suppliers = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, name: '', contactPerson: '', phone: '', address: '' })

async function fetchData() { suppliers.value = await getSupplierList() }
function edit(row) { isEdit.value = true; Object.assign(form, row); showDialog.value = true }
async function handleSave() { isEdit.value ? await updateSupplier(form) : await addSupplier(form); ElMessage.success('保存成功'); showDialog.value = false; form.id = null; form.name = form.contactPerson = form.phone = form.address = ''; isEdit.value = false; fetchData() }
async function handleDelete(id) { await deleteSupplier(id); ElMessage.success('已删除'); fetchData() }

onMounted(fetchData)
</script>
