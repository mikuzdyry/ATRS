<template>
  <div class="page-container">
    <div class="page-header"><h1>用户管理</h1></div>
    <el-table :data="users" stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="phone" label="手机" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="角色"><template #default="{row}"><el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'">{{ row.role }}</el-tag></template></el-table-column>
      <el-table-column prop="createdAt" label="注册时间" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'

const users = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { const r = await api.get('/user/admin/list'); users.value = r.data } finally { loading.value = false }
})
</script>
