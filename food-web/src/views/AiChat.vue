<template>
    <el-row :gutter="20">
        <el-col :span="16">
            <el-card shadow="hover" style="height:calc(100vh - 160px);display:flex;flex-direction:column;">
                <template #header><span style="font-weight:bold;">🤖 AI 智能客服 — 食材管理助手</span></template>
                <div ref="chatBox" class="chat-box">
                    <div v-for="(msg, i) in messages" :key="i" :class="'msg ' + msg.role">
                        <el-avatar :size="36" :icon="msg.role === 'user' ? 'UserFilled' : 'MagicStick'" :style="{background: msg.role === 'user' ? '#409EFF' : '#67C23A'}" />
                        <div class="bubble">
                            <div class="role-tag">{{ msg.role === 'user' ? '我' : 'AI助手' }}</div>
                            <div class="text">{{ msg.content }}</div>
                        </div>
                    </div>
                </div>
                <div class="input-area">
                    <el-input v-model="inputMsg" placeholder="输入你的食材管理问题..." size="large" @keyup.enter="sendMessage" :disabled="loading">
                        <template #append>
                            <el-button type="primary" @click="sendMessage" :loading="loading">发送</el-button>
                        </template>
                    </el-input>
                </div>
            </el-card>
        </el-col>
        <el-col :span="8">
            <el-card shadow="hover">
                <template #header><span style="font-weight:bold;">💡 推荐问题</span></template>
                <div v-for="q in questions" :key="q" class="q-item" @click="quickAsk(q)">
                    <el-icon><ChatDotSquare /></el-icon>
                    <span>{{ q }}</span>
                </div>
            </el-card>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { sendChat } from '@/api'

const chatBox = ref(null)
const inputMsg = ref('')
const loading = ref(false)
const messages = ref([
    { role: 'assistant', content: '你好！我是 AI 食材管理助手，可以帮你解答食材采购、库存管理、菜谱推荐等问题。请问有什么可以帮你的？' }
])

const questions = [
    '如何减少食材损耗？',
    '帮我列一份本周采购清单',
    '蔬菜类食材如何储存更保鲜？',
    '食材保质期管理有什么技巧？',
    '分析一下最近的损耗原因',
    '推荐一份低成本的菜品方案'
]

async function sendMessage() {
    if (!inputMsg.value.trim()) return
    const msg = inputMsg.value
    messages.value.push({ role: 'user', content: msg })
    inputMsg.value = ''
    loading.value = true
    try {
        const reply = await sendChat(msg, 'web_session')
        messages.value.push({ role: 'assistant', content: reply || '暂无回复' })
    } catch (e) {
        messages.value.push({ role: 'assistant', content: '抱歉，AI 服务暂不可用，请稍后再试。' })
    } finally {
        loading.value = false
        scrollToBottom()
    }
}

function quickAsk(q) {
    inputMsg.value = q
    sendMessage()
}

function scrollToBottom() {
    nextTick(() => {
        if (chatBox.value) chatBox.value.scrollTop = chatBox.value.scrollHeight
    })
}
</script>

<style scoped>
.chat-box { flex: 1; overflow-y: auto; padding: 10px 0; display: flex; flex-direction: column; gap: 16px; }
.msg { display: flex; gap: 12px; max-width: 85%; }
.msg.user { flex-direction: row-reverse; align-self: flex-end; }
.msg.assistant { align-self: flex-start; }
.bubble { background: #f5f7fa; padding: 12px 16px; border-radius: 12px; }
.msg.user .bubble { background: #ecf5ff; }
.role-tag { font-size: 12px; color: #909399; margin-bottom: 4px; }
.text { line-height: 1.6; white-space: pre-wrap; }
.input-area { padding-top: 12px; }
.q-item { display: flex; align-items: center; gap: 8px; padding: 10px 0; cursor: pointer; color: #409EFF; border-bottom: 1px solid #f0f0f0; }
.q-item:hover { color: #66b1ff; }
</style>
