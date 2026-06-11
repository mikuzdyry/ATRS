<template>
  <div class="page-container" style="max-width: 400px; margin-top: 60px">
    <el-card>
      <template #header><h2 style="text-align:center">用户登录</h2></template>
      <el-form :model="form" label-position="top" @submit.prevent="handleLogin">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item><el-button type="primary" native-type="submit" :loading="loading" style="width:100%">登录</el-button></el-form-item>
      </el-form>
      <div class="text-center" style="color:#909399">还没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册</el-link></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const form = ref({ username: '', password: '' })
const loading = ref(false)

async function handleLogin() {
  loading.value = true
  try {
    await userStore.login(form.value)
    ElMessage.success('登录成功')
    router.push('/')
  } finally { loading.value = false }
}
</script>
