<template>
    <el-row :gutter="20">
        <el-col :span="14">
            <el-card shadow="hover">
                <template #header><span style="font-weight:bold;">📝 AI 内容生成</span></template>
                <el-tabs v-model="activeTab">
                    <el-tab-pane label="📖 生成菜谱" name="recipe">
                        <el-input v-model="recipePrompt" type="textarea" :rows="4" placeholder="请输入食材清单，如：鸡蛋3个、番茄2个、葱适量" />
                        <el-button type="primary" style="margin-top:12px;" :loading="recipeLoading" @click="genRecipe">AI 生成菜谱</el-button>
                        <div v-if="recipeResult" class="result-box">{{ recipeResult }}</div>
                    </el-tab-pane>
                    <el-tab-pane label="📊 采购报表" name="report">
                        <p style="color:#909399;margin-bottom:12px;">基于当前库存预警数据，自动生成采购计划报表</p>
                        <el-button type="primary" :loading="reportLoading" @click="genReport">生成采购报表</el-button>
                        <div v-if="reportResult" class="result-box">{{ reportResult }}</div>
                    </el-tab-pane>
                    <el-tab-pane label="🎨 AI 绘图" name="image">
                        <el-input v-model="imagePrompt" type="textarea" :rows="3" placeholder="输入图片描述词，如：食堂清淡菜品海报，简约风格" />
                        <el-button type="primary" style="margin-top:12px;" :loading="imageLoading" @click="genImage">生成图片</el-button>
                        <div v-if="imageUrl" style="margin-top:12px;">
                            <el-image :src="imageUrl" fit="contain" style="max-width:100%;border-radius:8px;" />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="📋 损耗分析" name="loss">
                        <el-input v-model="lossPrompt" type="textarea" :rows="4" placeholder="输入损耗数据描述，如：本月西红柿损耗20斤，原因腐烂；青菜损耗15斤，原因过期" />
                        <el-button type="primary" style="margin-top:12px;" :loading="lossLoading" @click="genLossReport">生成分析报告</el-button>
                        <div v-if="lossResult" class="result-box">{{ lossResult }}</div>
                    </el-tab-pane>
                </el-tabs>
            </el-card>
        </el-col>
        <el-col :span="10">
            <el-card shadow="hover">
                <template #header><span style="font-weight:bold;">📚 生成历史</span></template>
                <el-table :data="historyList" stripe size="small" @row-click="showHistory">
                    <el-table-column prop="title" label="标题" min-width="120" />
                    <el-table-column prop="generateType" label="类型" width="70">
                        <template #default="{row}"><el-tag size="small">{{ typeMap[row.generateType] }}</el-tag></template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="时间" width="80">
                        <template #default="{row}">{{ row.createTime?.substring(0, 10) }}</template>
                    </el-table-column>
                </el-table>
            </el-card>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { generateRecipe, generatePurchaseReport, generateLossReport, generateImage, getGenerateHistory } from '@/api'

const activeTab = ref('recipe')
const recipePrompt = ref('')
const recipeResult = ref('')
const recipeLoading = ref(false)
const reportResult = ref('')
const reportLoading = ref(false)
const imagePrompt = ref('')
const imageUrl = ref('')
const imageLoading = ref(false)
const lossPrompt = ref('')
const lossResult = ref('')
const lossLoading = ref(false)
const historyList = ref([])
const typeMap = { recipe: '菜谱', report: '报表', poster: '海报', analysis: '分析' }

async function genRecipe() {
    if (!recipePrompt.value.trim()) return ElMessage.warning('请输入食材清单')
    recipeLoading.value = true; recipeResult.value = ''
    try { const res = await generateRecipe(recipePrompt.value); recipeResult.value = res.content } catch (e) { ElMessage.error('生成失败') } finally { recipeLoading.value = false; fetchHistory() }
}

async function genReport() {
    reportLoading.value = true; reportResult.value = ''
    try { const res = await generatePurchaseReport(); reportResult.value = res.content } catch (e) { ElMessage.error('生成失败') } finally { reportLoading.value = false; fetchHistory() }
}

async function genImage() {
    if (!imagePrompt.value.trim()) return ElMessage.warning('请输入描述词')
    imageLoading.value = true; imageUrl.value = ''
    try { const res = await generateImage(imagePrompt.value); imageUrl.value = res.imageUrl; if (!imageUrl.value) ElMessage.warning('图片生成返回为空') } catch (e) { ElMessage.error('生成失败') } finally { imageLoading.value = false; fetchHistory() }
}

async function genLossReport() {
    if (!lossPrompt.value.trim()) return ElMessage.warning('请输入损耗数据')
    lossLoading.value = true; lossResult.value = ''
    try { const res = await generateLossReport(lossPrompt.value); lossResult.value = res.content } catch (e) { ElMessage.error('生成失败') } finally { lossLoading.value = false; fetchHistory() }
}

function showHistory(row) {
    if (row.content) {
        ElMessage.info('内容较长，请查看详情')
    } else if (row.imageUrl) {
        imageUrl.value = row.imageUrl
    }
}

async function fetchHistory() {
    try { historyList.value = await getGenerateHistory() } catch (e) { /* ignore */ } }
onMounted(fetchHistory)
</script>

<style scoped>
.result-box { margin-top:12px; padding:12px; background:#f5f7fa; border-radius:8px; line-height:1.8; white-space:pre-wrap; max-height:400px; overflow-y:auto; }
</style>
