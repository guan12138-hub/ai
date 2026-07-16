<template>
    <view class="page">
        <view class="chat-container">
            <scroll-view class="chat-box" scroll-y :scroll-into-view="scrollId">
                <view v-for="(msg, i) in messages" :key="i" :id="'msg-'+i" :class="'msg ' + msg.role">
                    <view class="bubble">
                        <text class="role-tag">{{ msg.role === 'user' ? '我' : 'AI助手' }}</text>
                        <text class="text">{{ msg.content }}</text>
                    </view>
                </view>
            </scroll-view>
            <view class="input-area">
                <input v-model="inputMsg" class="input" placeholder="输入食材管理问题..." confirm-type="send" @confirm="sendMessage" />
                <button class="send-btn" @click="sendMessage" :disabled="loading">发送</button>
            </view>
        </view>
    </view>
</template>

<script>
import api from '@/api'
export default {
    data() {
        return {
            inputMsg: '',
            loading: false,
            messages: [{ role: 'assistant', content: '你好！我是 AI 食材管理助手，请问有什么可以帮你的？' }],
            scrollId: ''
        }
    },
    methods: {
        async sendMessage() {
            if (!this.inputMsg.trim()) return
            const msg = this.inputMsg
            this.messages.push({ role: 'user', content: msg })
            this.inputMsg = ''
            this.loading = true
            try {
                const reply = await api.sendChat(msg, 'miniapp_session')
                this.messages.push({ role: 'assistant', content: reply || '暂无回复' })
            } catch(e) {
                this.messages.push({ role: 'assistant', content: 'AI 服务暂不可用，请稍后再试。' })
            } finally {
                this.loading = false
                this.$nextTick(() => { this.scrollId = 'msg-' + (this.messages.length - 1) })
            }
        }
    }
}
</script>

<style>
.page { height: 100vh; display: flex; flex-direction: column; }
.chat-container { flex: 1; display: flex; flex-direction: column; }
.chat-box { flex: 1; padding: 15px; }
.msg { margin-bottom: 15px; display: flex; }
.msg.user { justify-content: flex-end; }
.msg.assistant { justify-content: flex-start; }
.bubble { max-width: 80%; padding: 10px 14px; border-radius: 12px; background: #f5f7fa; }
.msg.user .bubble { background: #ecf5ff; }
.role-tag { font-size: 11px; color: #909399; display: block; margin-bottom: 4px; }
.text { font-size: 14px; line-height: 1.6; }
.input-area { display: flex; padding: 10px 15px; background: #fff; border-top: 1px solid #eee; }
.input { flex: 1; height: 40px; border: 1px solid #dcdfe6; border-radius: 20px; padding: 0 15px; font-size: 14px; }
.send-btn { margin-left: 10px; height: 40px; line-height: 40px; padding: 0 20px; background: #409EFF; color: #fff; border: none; border-radius: 20px; font-size: 14px; }
</style>
