<template>
  <div class="page-container" style="max-width: 800px">
    <div class="page-header"><h1>我的订单</h1></div>
    <el-table :data="orders" stripe v-loading="loading" empty-text="暂无订单">
      <el-table-column prop="orderNo" label="订单号" />
      <el-table-column label="航班"><template #default="{row}">{{ row.flight?.airline }} {{ row.flight?.flightNo }}</template></el-table-column>
      <el-table-column label="路线"><template #default="{row}">{{ row.flight?.departureCity }} → {{ row.flight?.arrivalCity }}</template></el-table-column>
      <el-table-column prop="passengerCount" label="人数" width="60" />
      <el-table-column label="金额"><template #default="{row}">¥{{ row.totalPrice }}</template></el-table-column>
      <el-table-column label="状态"><template #default="{row}"><el-tag :type="statusType(row.status)">{{ statusLabel(row.status) }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="$router.push(`/order/${row.orderNo}`)">详情</el-button>
          <el-button v-if="row.status === 'PENDING'" size="small" type="danger" @click="cancel(row.orderNo)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { ElMessage } from 'element-plus'

const orders = ref([])
const loading = ref(false)

function statusType(s) { return { PENDING: 'warning', PAID: 'success', CANCELLED: 'info', REFUNDED: 'info' }[s] || 'info' }
function statusLabel(s) { return { PENDING: '待支付', PAID: '已支付', CANCELLED: '已取消', REFUNDED: '已退款' }[s] || s }

onMounted(async () => {
  loading.value = true
  try { const r = await api.get('/order/list'); orders.value = r.data } finally { loading.value = false }
})

async function cancel(orderNo) {
  await api.post(`/order/cancel/${orderNo}`)
  ElMessage.success('已取消')
  const r = await api.get('/order/list'); orders.value = r.data
}
</script>
