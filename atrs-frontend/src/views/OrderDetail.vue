<template>
  <div class="page-container" style="max-width: 700px; margin-top: 30px">
    <el-card v-if="order" v-loading="loading">
      <template #header><h2>订单详情 — {{ order.orderNo }}</h2></template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="statusType(order.status)">{{ statusLabel(order.status) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="航班">{{ order.flight?.airline }} {{ order.flight?.flightNo }}</el-descriptions-item>
        <el-descriptions-item label="日期">{{ order.flight?.flightDate }}</el-descriptions-item>
        <el-descriptions-item label="出发">{{ order.flight?.departureCity }} {{ order.flight?.departureAirport }}</el-descriptions-item>
        <el-descriptions-item label="到达">{{ order.flight?.arrivalCity }} {{ order.flight?.arrivalAirport }}</el-descriptions-item>
        <el-descriptions-item label="舱位">{{ seatClassLabel(order.seatClass) }}</el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ order.totalPrice }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ order.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.contactPhone }}</el-descriptions-item>
      </el-descriptions>

      <h3 class="mt-20">乘客信息</h3>
      <el-table :data="order.passengers" stripe>
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="idCard" label="证件号" />
        <el-table-column prop="ticketNo" label="票号" />
      </el-table>

      <div v-if="order.paymentRecord" class="mt-20">
        <h3>支付记录</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="交易号">{{ order.paymentRecord.transactionNo }}</el-descriptions-item>
          <el-descriptions-item label="金额">¥{{ order.paymentRecord.amount }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ order.paymentRecord.paidAt }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'

const route = useRoute()
const order = ref(null)
const loading = ref(false)

function statusType(s) { return { PENDING: 'warning', PAID: 'success', CANCELLED: 'info', REFUNDED: 'info' }[s] || 'info' }
function statusLabel(s) { return { PENDING: '待支付', PAID: '已支付', CANCELLED: '已取消', REFUNDED: '已退款' }[s] || s }
function seatClassLabel(c) { return { ECONOMY: '经济舱', BUSINESS: '商务舱', FIRST: '头等舱' }[c] || c }

onMounted(async () => {
  loading.value = true
  try { const r = await api.get(`/order/detail/${route.params.orderNo}`); order.value = r.data } finally { loading.value = false }
})
</script>
