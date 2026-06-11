<template>
  <div class="page-container" style="max-width: 700px; margin-top: 30px">
    <el-card>
      <template #header><h2>预订机票</h2></template>
      <el-steps :active="1" class="mb-20"><el-step title="选择舱位" /><el-step title="填写信息" /><el-step title="确认支付" /></el-steps>

      <el-form :model="form" label-position="top" ref="formRef">
        <el-form-item label="联系人姓名" required><el-input v-model="form.contactName" /></el-form-item>
        <el-form-item label="联系电话" required><el-input v-model="form.contactPhone" /></el-form-item>
        <el-form-item label="联系邮箱"><el-input v-model="form.contactEmail" /></el-form-item>
        <el-divider>乘客信息</el-divider>
        <div v-for="(p, i) in form.passengers" :key="i" class="passenger-row">
          <h4>乘客 {{ i + 1 }}</h4>
          <el-form-item label="姓名" required><el-input v-model="p.name" /></el-form-item>
          <el-form-item label="证件号" required><el-input v-model="p.idCard" /></el-form-item>
          <el-form-item label="手机号"><el-input v-model="p.phone" /></el-form-item>
          <el-button v-if="form.passengers.length > 1" type="danger" size="small" @click="removePassenger(i)">移除</el-button>
        </div>
        <el-button class="mt-20" @click="addPassenger">+ 添加乘客</el-button>
        <el-divider />
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="submitting" @click="submit">提交订单</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const flight = ref(null)
const submitting = ref(false)

const form = ref({
  flightId: Number(route.params.flightId),
  seatClass: route.query.seatClass || 'ECONOMY',
  passengers: [{ name: '', idCard: '', phone: '' }],
  contactName: '', contactPhone: '', contactEmail: '', remark: ''
})

function addPassenger() { form.value.passengers.push({ name: '', idCard: '', phone: '' }) }
function removePassenger(i) { form.value.passengers.splice(i, 1) }

async function submit() {
  submitting.value = true
  try {
    const res = await api.post('/order/create', form.value)
    ElMessage.success('下单成功')
    router.push(`/booking/confirm/${res.data.orderNo}`)
  } finally { submitting.value = false }
}
</script>

<style scoped>
.passenger-row { padding: 12px; margin-bottom: 12px; background: #f8f9fa; border-radius: 8px; }
</style>
