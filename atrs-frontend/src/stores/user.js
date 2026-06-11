import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../api'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const role = ref(localStorage.getItem('role') || '')
  const username = ref(localStorage.getItem('username') || '')

  async function login(credentials) {
    const res = await api.post('/user/login', credentials)
    token.value = res.data.token
    localStorage.setItem('token', res.data.token)
    await fetchProfile()
    return res
  }

  async function register(data) {
    return await api.post('/user/register', data)
  }

  async function fetchProfile() {
    try {
      const res = await api.get('/user/profile')
      role.value = res.data.role
      username.value = res.data.username
      localStorage.setItem('role', res.data.role)
      localStorage.setItem('username', res.data.username)
    } catch (e) { /* ignore */ }
  }

  function logout() {
    token.value = ''
    role.value = ''
    username.value = ''
    localStorage.clear()
  }

  return { token, role, username, login, register, fetchProfile, logout }
})
