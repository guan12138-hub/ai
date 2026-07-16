<template>
    <div class="dashboard">
        <div class="stats-row">
            <el-card shadow="hover" v-for="s in stats" :key="s.label" class="stat-card">
                <div class="stat-inner">
                    <div>
                        <div class="stat-value">{{ s.value }}</div>
                        <div class="stat-label">{{ s.label }}</div>
                    </div>
                    <el-icon :size="40" :color="s.color"><component :is="s.icon" /></el-icon>
                </div>
            </el-card>
        </div>

        <el-row :gutter="20" style="margin-top:20px;">
            <el-col :span="14">
                <el-card shadow="hover">
                    <template #header><span style="font-weight:bold;">⚡ 库存预警</span></template>
                    <el-table :data="warningList" stripe style="width:100%" v-if="warningList.length">
                        <el-table-column prop="name" label="食材" />
                        <el-table-column prop="stockQuantity" label="库存" />
                        <el-table-column prop="unit" label="单位" width="60" />
                        <el-table-column prop="warningThreshold" label="阈值" />
                        <el-table-column label="状态">
                            <template #default="{row}">
                                <el-tag :type="row.stockQuantity <= 0 ? 'danger' : 'warning'" size="small">
                                    {{ row.stockQuantity <= 0 ? '缺货' : '预警' }}
                                </el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-empty v-else description="暂无预警" />
                </el-card>
            </el-col>
            <el-col :span="10">
                <el-card shadow="hover">
                    <template #header><span style="font-weight:bold;">📅 临期提醒</span></template>
                    <el-table :data="expiringList" stripe style="width:100%" v-if="expiringList.length">
                        <el-table-column prop="name" label="食材" />
                        <el-table-column prop="expiryDate" label="保质期" width="110" />
                        <el-table-column label="状态">
                            <template #default="{row}">
                                <el-tag type="danger" size="small">临期</el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-empty v-else description="暂无临期食材" />
                </el-card>
            </el-col>
        </el-row>

        <el-card shadow="hover" style="margin-top:20px;">
            <template #header><span style="font-weight:bold;">📊 库存分类分布</span></template>
            <div ref="chartRef" style="height:300px;"></div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { getWarningStock, getExpiringStock, getStockPage } from '@/api'
import * as echarts from 'echarts'

const stats = ref([
    { label: '食材种类', value: '--', icon: 'Goods', color: '#409EFF' },
    { label: '低库存预警', value: '--', icon: 'WarningFilled', color: '#E6A23C' },
    { label: '临期食材', value: '--', icon: 'Clock', color: '#F56C6C' },
    { label: '供应商', value: '--', icon: 'UserFilled', color: '#67C23A' }
])
const warningList = ref([])
const expiringList = ref([])
const chartRef = ref(null)
let chart = null

onMounted(async () => {
    try {
        const page = await getStockPage({ page: 1, size: 1000 })
        stats.value[0].value = page.records?.length || 0

        const w = await getWarningStock()
        warningList.value = w || []
        stats.value[1].value = w?.length || 0

        const e = await getExpiringStock()
        expiringList.value = e || []
        stats.value[2].value = e?.length || 0

        // 分类统计
        const categoryMap = {}
        ;(page.records || []).forEach(item => {
            categoryMap[item.category] = (categoryMap[item.category] || 0) + 1
        })
        initChart(Object.keys(categoryMap), Object.values(categoryMap))
    } catch (e) { /* ignore */ }
})

function initChart(categories, values) {
    if (!chartRef.value) return
    chart = echarts.init(chartRef.value)
    chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: '0%' },
        series: [{
            type: 'pie',
            radius: ['40%', '65%'],
            avoidLabelOverlap: true,
            itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
            label: { show: false },
            emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
            data: categories.map((name, i) => ({
                name,
                value: values[i],
                itemStyle: { color: ['#409EFF','#67C23A','#E6A23C','#F56C6C','#909399','#B37FEB'][i % 6] }
            }))
        }]
    })
}

onUnmounted(() => { chart?.dispose() })
</script>

<style scoped>
.stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.stat-card { border-radius: 12px; }
.stat-inner { display: flex; justify-content: space-between; align-items: center; }
.stat-value { font-size: 28px; font-weight: bold; color: #303133; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
</style>
