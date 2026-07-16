<template>
    <div class="analysis-page">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="action-card">
                    <template #header><span style="font-weight:bold;">损耗趋势分析</span></template>
                    <el-form label-width="70px">
                        <el-form-item label="开始日期"><el-date-picker v-model="lossStart" type="date" value-format="YYYY-MM-DD" /></el-form-item>
                        <el-form-item label="结束日期"><el-date-picker v-model="lossEnd" type="date" value-format="YYYY-MM-DD" /></el-form-item>
                        <el-button type="primary" :loading="lossLoading" @click="handleLossTrend" style="width:100%;">开始分析</el-button>
                    </el-form>
                    <div v-if="lossSummary" class="summary">{{ lossSummary }}</div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="action-card">
                    <template #header><span style="font-weight:bold;">库存成本分析</span></template>
                    <p style="color:#909399;margin-bottom:12px;">分析当前所有库存食材的成本分布，优化采购策略</p>
                    <el-button type="success" :loading="costLoading" @click="handleCost" style="width:100%;">开始分析</el-button>
                    <div v-if="costSummary" class="summary">{{ costSummary }}</div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="action-card">
                    <template #header><span style="font-weight:bold;">智能预警分析</span></template>
                    <p style="color:#909399;margin-bottom:12px;">检测低库存和临期食材，给出优先级建议</p>
                    <el-button type="warning" :loading="warnLoading" @click="handleWarning" style="width:100%;">开始分析</el-button>
                    <div v-if="warnSummary" class="summary">{{ warnSummary }}</div>
                </el-card>
            </el-col>
        </el-row>

        <el-card shadow="hover" style="margin-top:20px;">
            <template #header><span style="font-weight:bold;">分析报告历史</span></template>
            <el-table :data="reports" stripe @row-click="showReport">
                <el-table-column prop="reportTitle" label="报告标题" min-width="160" />
                <el-table-column prop="reportType" label="类型" width="100">
                    <template #default="{row}"><el-tag>{{ reportTypeMap[row.reportType] }}</el-tag></template>
                </el-table-column>
                <el-table-column prop="periodStart" label="起始" width="100" />
                <el-table-column prop="periodEnd" label="截止" width="100" />
                <el-table-column prop="createTime" label="生成时间" width="160" />
            </el-table>
        </el-card>

        <el-dialog v-model="showDetail" title="分析详情" width="700px">
            <div v-if="currentReport?.summary" style="white-space:pre-wrap;line-height:1.8;">{{ currentReport.summary }}</div>
            <div v-else>暂无详情</div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { analyzeLossTrend, analyzeCost, analyzeWarning, getAnalysisReports } from '@/api'

const lossStart = ref(''), lossEnd = ref('')
const lossSummary = ref(''), lossLoading = ref(false)
const costSummary = ref(''), costLoading = ref(false)
const warnSummary = ref(''), warnLoading = ref(false)
const reports = ref([])
const showDetail = ref(false)
const currentReport = ref(null)
const reportTypeMap = { loss_trend: '损耗趋势', cost_analysis: '成本分析', warning: '预警分析', stock_forecast: '库存预测' }

async function handleLossTrend() {
    if (!lossStart.value || !lossEnd.value) return ElMessage.warning('请选择日期范围')
    lossLoading.value = true
    try { const res = await analyzeLossTrend({ startDate: lossStart.value, endDate: lossEnd.value }); lossSummary.value = res.summary; ElMessage.success('分析完成') } catch (e) { ElMessage.error('分析失败') } finally { lossLoading.value = false; fetchReports() }
}

async function handleCost() {
    costLoading.value = true
    try { const res = await analyzeCost(); costSummary.value = res.summary; ElMessage.success('分析完成') } catch (e) { ElMessage.error('分析失败') } finally { costLoading.value = false; fetchReports() }
}

async function handleWarning() {
    warnLoading.value = true
    try { const res = await analyzeWarning(); warnSummary.value = res.summary; ElMessage.success('分析完成') } catch (e) { ElMessage.error('分析失败') } finally { warnLoading.value = false; fetchReports() }
}

function showReport(row) { currentReport.value = row; showDetail.value = true }

async function fetchReports() { try { reports.value = await getAnalysisReports() } catch (e) { /* ignore */ } }
onMounted(fetchReports)
</script>

<style scoped>
.action-card { height: 320px; display: flex; flex-direction: column; }
.summary { margin-top: 12px; padding: 10px; background: #f5f7fa; border-radius: 8px; font-size: 13px; line-height: 1.6; max-height: 140px; overflow-y: auto; }
</style>
