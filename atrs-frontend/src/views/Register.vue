<template>
  <div class="page-container" style="max-width: 400px; margin-top: 60px">
    <el-card>
      <template #header><h2 style="text-align:center">用户注册</h2></template>
      <el-form :model="form" label-position="top" @submit.prevent="handleRegister">
        <el-form-item label="用户名"><el-input v-model="form.username" placeholder="3-50位" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" placeholder="至少6位" show-password /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="form.confirmPassword" type="password" show-password /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item><el-button type="primary" native-type="submit" :loading="loading" style="width:100%">注册</el-button></el-form-item>
      </el-form>
      <div class="text-center">已有账号？<el-link type="primary" @click="$router.push('/login')">去登录</el-link></div>
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
const form = ref({ username: '', password: '', confirmPassword: '', realName: '', phone: '', email: '' })
const loading = ref(false)

async function handleRegister() {
  if (form.value.password !== form.value.confirmPassword) { ElMessage.error('两次密码不一致'); return }
  loading.value = true
  try {
    await userStore.register(form.value)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally { loading.value = false }
}
</script>
