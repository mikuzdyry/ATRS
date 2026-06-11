<template>
  <div class="page-container" style="max-width: 800px; margin-top: 30px">
    <el-card v-if="flight" v-loading="loading">
      <template #header><h2>{{ flight.airline }} {{ flight.flightNo }}</h2></template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="出发城市">{{ flight.departureCity }}</el-descriptions-item>
        <el-descriptions-item label="到达城市">{{ flight.arrivalCity }}</el-descriptions-item>
        <el-descriptions-item label="出发机场">{{ flight.departureAirport }}</el-descriptions-item>
        <el-descriptions-item label="到达机场">{{ flight.arrivalAirport }}</el-descriptions-item>
        <el-descriptions-item label="出发时间">{{ flight.departureTime }}</el-descriptions-item>
        <el-descriptions-item label="到达时间">{{ flight.arrivalTime }}</el-descriptions-item>
        <el-descriptions-item label="飞行时长">{{ flight.durationMinutes }} 分钟</el-descriptions-item>
        <el-descriptions-item label="日期">{{ flight.flightDate }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag>{{ flight.status }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="基准票价">¥{{ flight.basePrice }}</el-descriptions-item>
      </el-descriptions>

      <h3 class="mt-20">舱位选择</h3>
      <el-row :gutter="16">
        <el-col :span="8" v-for="seat in flight.seats" :key="seat.id">
          <el-card :class="['seat-card', { disabled: seat.availableSeats <= 0 }]">
            <h3>{{ seatClassLabel(seat.seatClass) }}</h3>
            <p class="price">¥{{ seat.price }}</p>
            <p>剩余 {{ seat.availableSeats }} / {{ seat.seatCount }} 座</p>
            <el-button type="primary" :disabled="seat.availableSeats <= 0"
              @click="$router.push(`/booking/${flight.id}?seatClass=${seat.seatClass}`)">
              {{ seat.availableSeats > 0 ? '预订' : '已售罄' }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'

const route = useRoute()
const flight = ref(null)
const loading = ref(false)

function seatClassLabel(c) { return { ECONOMY: '经济舱', BUSINESS: '商务舱', FIRST: '头等舱' }[c] || c }

onMounted(async () => {
  loading.value = true
  try { const r = await api.get(`/flight/${route.params.id}`); flight.value = r.data } finally { loading.value = false }
})
</script>

<style scoped>
.seat-card { text-align: center; }
.seat-card.disabled { opacity: 0.5; }
.seat-card .price { font-size: 28px; color: #e6a23c; font-weight: bold; }
</style>
