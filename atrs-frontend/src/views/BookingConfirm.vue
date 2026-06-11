<template>
  <div class="page-container" style="max-width: 700px; margin-top: 30px">
    <el-card v-if="order" v-loading="loading">
      <template #header><h2>订单确认</h2></template>
      <el-result icon="success" title="下单成功！" sub-title="请在 30 分钟内完成支付">
        <template #extra>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="航班">{{ order.flight?.airline }} {{ order.flight?.flightNo }}</el-descriptions-item>
            <el-descriptions-item label="路线">{{ order.flight?.departureCity }} → {{ order.flight?.arrivalCity }}</el-descriptions-item>
            <el-descriptions-item label="舱位">{{ seatClassLabel(order.seatClass) }}</el-descriptions-item>
            <el-descriptions-item label="乘客数">{{ order.passengerCount }} 人</el-descriptions-item>
            <el-descriptions-item label="总价"><span style="color:#e6a23c;font-size:20px;font-weight:bold">¥{{ order.totalPrice }}</span></el-descriptions-item>
            <el-descriptions-item label="状态"><el-tag>{{ order.status }}</el-tag></el-descriptions-item>
          </el-descriptions>
        </template>
      </el-result>
      <el-button v-if="order.status === 'PENDING'" type="success" size="large" style="width:100%" @click="pay">立即支付 ¥{{ order.totalPrice }}</el-button>
      <el-button class="mt-20" @click="$router.push('/orders')">查看我的订单</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const order = ref(null)
const loading = ref(false)

function seatClassLabel(c) { return { ECONOMY: '经济舱', BUSINESS: '商务舱', FIRST: '头等舱' }[c] || c }

onMounted(async () => {
  loading.value = true
  try { const r = await api.get(`/order/detail/${route.params.orderNo}`); order.value = r.data } finally { loading.value = false }
})

async function pay() {
  await api.post(`/order/pay/${order.value.orderNo}`)
  ElMessage.success('支付成功！')
  const r = await api.get(`/order/detail/${order.value.orderNo}`); order.value = r.data
}
</script>
