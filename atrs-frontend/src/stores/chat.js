import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../api'

export const useChatStore = defineStore('chat', () => {
  const messages = ref([])
  const sessionId = ref('session-' + Date.now())
  const visible = ref(false)

  async function send(message) {
    messages.value.push({ role: 'user', content: message })
    try {
      const res = await api.post('/llm/chat', { message, sessionId: sessionId.value })
      messages.value.push({ role: 'assistant', content: res.data.reply })
    } catch (e) {
      messages.value.push({ role: 'assistant', content: '抱歉，AI客服暂时不可用，请稍后再试。' })
    }
  }

  function toggle() {
    visible.value = !visible.value
  }

  return { messages, sessionId, visible, send, toggle }
})
