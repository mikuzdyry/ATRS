<template>
  <div class="chat-widget">
    <el-button v-if="!chatStore.visible" class="chat-toggle" type="primary" circle @click="chatStore.toggle()">
      <el-icon size="24"><ChatDotRound /></el-icon>
    </el-button>

    <el-card v-else class="chat-panel" shadow="always">
      <template #header>
        <div class="chat-header">
          <span>🤖 AI 智能客服</span>
          <el-button text @click="chatStore.toggle()"><el-icon><Close /></el-icon></el-button>
        </div>
      </template>
      <div class="chat-body" ref="chatBody">
        <div v-for="(msg, i) in chatStore.messages" :key="i" :class="['msg', msg.role]">
          {{ msg.content }}
        </div>
        <div v-if="chatStore.messages.length === 0" class="hint">
          你好！我是 ATRS 智能客服，可以帮你查询航班、解答疑问。请问有什么可以帮你的？
        </div>
      </div>
      <div class="chat-input">
        <el-input v-model="inputText" placeholder="输入消息..." @keyup.enter="sendMsg" />
        <el-button type="primary" @click="sendMsg" :loading="sending">发送</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useChatStore } from '../stores/chat'

const chatStore = useChatStore()
const inputText = ref('')
const sending = ref(false)
const chatBody = ref(null)

async function sendMsg() {
  if (!inputText.value.trim()) return
  sending.value = true
  await chatStore.send(inputText.value.trim())
  inputText.value = ''
  sending.value = false
  await nextTick()
  if (chatBody.value) chatBody.value.scrollTop = chatBody.value.scrollHeight
}
</script>

<style scoped>
.chat-widget { position: fixed; bottom: 20px; right: 20px; z-index: 1000; }
.chat-toggle { width: 56px; height: 56px; font-size: 24px; }
.chat-panel { width: 360px; height: 500px; display: flex; flex-direction: column; }
.chat-header { display: flex; justify-content: space-between; align-items: center; }
.chat-body { flex: 1; overflow-y: auto; padding: 10px; font-size: 14px; }
.chat-body .msg { margin-bottom: 10px; padding: 10px 14px; border-radius: 12px; max-width: 85%; line-height: 1.6; }
.chat-body .msg.user { background: #409eff; color: #fff; margin-left: auto; }
.chat-body .msg.assistant { background: #f0f2f5; color: #303133; }
.chat-body .hint { color: #909399; text-align: center; margin-top: 40px; }
.chat-input { display: flex; gap: 8px; padding: 10px 0 0; border-top: 1px solid #ebeef5; }
</style>
