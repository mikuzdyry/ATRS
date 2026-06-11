<template>
  <div class="page-container" style="max-width: 700px; margin-top: 30px">
    <el-card v-if="order" v-loading="loading">
      <template #header><h2>订单详情 — {{ order.orderNo }}</h2></template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户">{{ order.user?.username }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag>{{ order.status }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ order.totalPrice }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ order.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ order.createdAt }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="order.passengers" class="mt-20" stripe>
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="idCard" label="证件号" />
        <el-table-column prop="ticketNo" label="票号" />
      </el-table>
      <el-button v-if="order.status === 'PENDING' || order.status === 'PAID'" class="mt-20" type="danger" @click="cancel">取消/退款</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try { const r = await api.get(`/order/admin/detail/${route.params.id}`); order.value = r.data } finally { loading.value = false }
})

async function cancel() {
  await api.post(`/order/admin/cancel/${order.value.id}`)
  ElMessage.success('已取消/退款')
  const r = await api.get(`/order/admin/detail/${order.value.id}`); order.value = r.data
}
</script>
