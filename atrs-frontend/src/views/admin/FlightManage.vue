<template>
  <div class="page-container">
    <div class="page-header" style="display:flex;justify-content:space-between">
      <h1>航班管理</h1>
      <el-button type="primary" @click="$router.push('/admin/flights/add')">添加航班</el-button>
    </div>
    <el-table :data="flights" stripe v-loading="loading">
      <el-table-column prop="flightNo" label="航班号" />
      <el-table-column prop="airline" label="航空公司" />
      <el-table-column label="路线"><template #default="{row}">{{ row.departureCity }} → {{ row.arrivalCity }}</template></el-table-column>
      <el-table-column prop="flightDate" label="日期" />
      <el-table-column prop="departureTime" label="起飞" />
      <el-table-column label="票价"><template #default="{row}">¥{{ row.basePrice }}</template></el-table-column>
      <el-table-column label="状态"><template #default="{row}"><el-tag>{{ row.status }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <el-button size="small" @click="$router.push(`/admin/flights/edit/${row.id}`)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'
import { ElMessage } from 'element-plus'

const flights = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { const r = await api.get('/flight/admin/list'); flights.value = r.data } finally { loading.value = false }
})

async function del(id) {
  await api.delete(`/flight/admin/delete/${id}`)
  ElMessage.success('删除成功')
  const r = await api.get('/flight/admin/list'); flights.value = r.data
}
</script>
