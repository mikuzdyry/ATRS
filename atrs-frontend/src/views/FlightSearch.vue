<template>
  <div class="page-container">
    <div class="page-header"><h1>航班搜索结果</h1></div>
    <el-table :data="flights" stripe v-loading="loading" empty-text="未找到航班">
      <el-table-column prop="flightNo" label="航班号" />
      <el-table-column prop="airline" label="航空公司" />
      <el-table-column prop="departureCity" label="出发" />
      <el-table-column prop="arrivalCity" label="到达" />
      <el-table-column prop="departureTime" label="起飞时间" />
      <el-table-column prop="arrivalTime" label="到达时间" />
      <el-table-column label="票价"><template #default="{row}">¥{{ row.basePrice }}</template></el-table-column>
      <el-table-column label="操作">
        <template #default="{row}">
          <el-button type="primary" size="small" @click="$router.push(`/flight/${row.id}`)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'

const route = useRoute()
const flights = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await api.get('/flight/search', { params: route.query })
    flights.value = res.data
  } finally { loading.value = false }
})
</script>
