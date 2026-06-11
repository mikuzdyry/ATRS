<template>
  <div class="page-container">
    <div class="page-header"><h1>订单管理</h1></div>
    <el-table :data="orders" stripe v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column label="用户"><template #default="{row}">{{ row.user?.username }}</template></el-table-column>
      <el-table-column label="航班"><template #default="{row}">{{ row.flight?.flightNo }}</template></el-table-column>
      <el-table-column label="金额"><template #default="{row}">¥{{ row.totalPrice }}</template></el-table-column>
      <el-table-column label="状态"><template #default="{row}"><el-tag>{{ row.status }}</el-tag></template></el-table-column>
      <el-table-column label="时间"><template #default="{row}">{{ row.createdAt }}</template></el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="$router.push(`/admin/orders/${row.id}`)">详情</el-button>
          <el-button v-if="row.status !== 'CANCELLED' && row.status !== 'REFUNDED'" size="small" type="danger" @click="cancel(row.id)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="mt-20" layout="prev,pager,next" :total="total" :page-size="20" @current-change="load" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../api'
import { ElMessage } from 'element-plus'

const orders = ref([])
const total = ref(0)
const loading = ref(false)

async function load(page = 0) {
  loading.value = true
  try {
    const r = await api.get('/order/admin/list', { params: { page, size: 20 } })
    orders.value = r.data.content
    total.value = r.data.totalElements
  } finally { loading.value = false }
}

onMounted(() => load())

async function cancel(id) {
  await api.post(`/order/admin/cancel/${id}`)
  ElMessage.success('已取消')
  load()
}
</script>
