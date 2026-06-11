<template>
  <div class="page-container" style="max-width: 600px; margin-top: 30px">
    <el-card v-if="user">
      <template #header><h2>个人中心</h2></template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ user.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机">{{ user.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证">{{ user.idCard || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-button type="primary" class="mt-20" @click="editVisible = true">编辑资料</el-button>

      <el-dialog v-model="editVisible" title="编辑资料" width="400px">
        <el-form :model="form" label-position="top">
          <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
          <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          <el-form-item label="身份证"><el-input v-model="form.idCard" /></el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editVisible = false">取消</el-button>
          <el-button type="primary" @click="updateProfile">保存</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { ElMessage } from 'element-plus'

const user = ref(null)
const editVisible = ref(false)
const form = ref({ realName: '', phone: '', email: '', idCard: '' })

onMounted(async () => {
  try { const r = await api.get('/user/profile'); user.value = r.data } catch {}
})

async function updateProfile() {
  await api.put('/user/profile', form.value)
  ElMessage.success('更新成功')
  editVisible.value = false
  const r = await api.get('/user/profile'); user.value = r.data
}
</script>
