<template>
  <div class="page-container" style="max-width: 700px; margin-top: 30px">
    <el-card>
      <template #header><h2>{{ isEdit ? '编辑航班' : '添加航班' }}</h2></template>
      <el-form :model="form" label-position="top">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="航班号"><el-input v-model="form.flightNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="航空公司"><el-input v-model="form.airline" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="出发城市"><el-input v-model="form.departureCity" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="到达城市"><el-input v-model="form.arrivalCity" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="出发机场"><el-input v-model="form.departureAirport" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="到达机场"><el-input v-model="form.arrivalAirport" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="日期"><el-date-picker v-model="form.flightDate" value-format="YYYY-MM-DD" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="起飞时间"><el-time-picker v-model="form.departureTime" value-format="HH:mm" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="到达时间"><el-time-picker v-model="form.arrivalTime" value-format="HH:mm" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="飞行时长(分钟)"><el-input-number v-model="form.durationMinutes" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="基准票价"><el-input-number v-model="form.basePrice" :precision="2" /></el-form-item></el-col>
        </el-row>
        <el-divider>舱位配置</el-divider>
        <div v-for="(s, i) in form.seats" :key="i" class="seat-row">
          <el-row :gutter="16">
            <el-col :span="8"><el-select v-model="s.seatClass"><el-option label="经济舱" value="ECONOMY" /><el-option label="商务舱" value="BUSINESS" /><el-option label="头等舱" value="FIRST" /></el-select></el-col>
            <el-col :span="6"><el-input-number v-model="s.seatCount" placeholder="座位数" /></el-col>
            <el-col :span="8"><el-input-number v-model="s.price" :precision="2" placeholder="票价" /></el-col>
            <el-col :span="2"><el-button v-if="form.seats.length > 1" @click="removeSeat(i)" icon="Delete" circle /></el-col>
          </el-row>
        </div>
        <el-button class="mt-20" @click="addSeat">+ 添加舱位</el-button>
        <el-divider />
        <el-button type="primary" size="large" style="width:100%" :loading="saving" @click="save">保存</el-button>
      </el-form>
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
const isEdit = ref(!!route.params.id)
const saving = ref(false)

const form = ref({
  flightNo: '', airline: '', departureCity: '', arrivalCity: '',
  departureAirport: '', arrivalAirport: '', departureTime: '', arrivalTime: '',
  durationMinutes: 120, flightDate: '', basePrice: 500,
  seats: [{ seatClass: 'ECONOMY', seatCount: 150, price: 500 }]
})

function addSeat() { form.value.seats.push({ seatClass: 'BUSINESS', seatCount: 20, price: 800 }) }
function removeSeat(i) { form.value.seats.splice(i, 1) }

onMounted(async () => {
  if (isEdit.value) {
    const r = await api.get(`/flight/admin/detail/${route.params.id}`)
    const f = r.data
    form.value = {
      flightNo: f.flightNo, airline: f.airline, departureCity: f.departureCity, arrivalCity: f.arrivalCity,
      departureAirport: f.departureAirport, arrivalAirport: f.arrivalAirport,
      departureTime: f.departureTime, arrivalTime: f.arrivalTime,
      durationMinutes: f.durationMinutes, flightDate: f.flightDate, basePrice: f.basePrice,
      seats: f.seats.map(s => ({ seatClass: s.seatClass, seatCount: s.seatCount, price: s.price }))
    }
  }
})

async function save() {
  saving.value = true
  try {
    if (isEdit.value) {
      await api.put(`/flight/admin/edit/${route.params.id}`, form.value)
    } else {
      await api.post('/flight/admin/add', form.value)
    }
    ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
    router.push('/admin/flights')
  } finally { saving.value = false }
}
</script>

<style scoped>
.seat-row { padding: 10px; margin-bottom: 8px; background: #f8f9fa; border-radius: 8px; }
</style>
