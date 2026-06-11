<template>
  <div class="page-container">
    <div class="page-header"><h1>管理后台</h1></div>
    <el-row :gutter="20">
      <el-col :span="6"><el-card class="stat-card"><h3>总用户</h3><p class="num">{{ stats.users }}</p></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><h3>总航班</h3><p class="num">{{ stats.flights }}</p></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><h3>总订单</h3><p class="num">{{ stats.orders }}</p></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><h3>已支付</h3><p class="num">{{ stats.paidOrders }}</p></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'

const stats = ref({ users: 0, flights: 0, orders: 0, paidOrders: 0 })

onMounted(async () => {
  try {
    const [u, f, o] = await Promise.all([
      api.get('/user/admin/list'),
      api.get('/flight/admin/list'),
      api.get('/order/admin/statistics')
    ])
    stats.value.users = u.data?.length || 0
    stats.value.flights = f.data?.length || 0
    stats.value.orders = o.data?.totalOrders || 0
    stats.value.paidOrders = o.data?.paidOrders || 0
  } catch {}
})
</script>

<style scoped>
.stat-card { text-align: center; padding: 10px; }
.stat-card .num { font-size: 36px; font-weight: bold; color: #409eff; margin: 10px 0; }
</style>
