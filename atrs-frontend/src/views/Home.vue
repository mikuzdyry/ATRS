<template>
  <div class="hero">
    <h1>ATRS 航空票务预订系统</h1>
    <p>搜索航班，轻松预订，畅享旅程</p>
    <div class="search-box">
      <el-form :model="form" inline @submit.prevent="search">
        <el-form-item><el-input v-model="form.departureCity" placeholder="出发城市" size="large" /></el-form-item>
        <el-form-item><el-input v-model="form.arrivalCity" placeholder="到达城市" size="large" /></el-form-item>
        <el-form-item><el-date-picker v-model="form.date" placeholder="出发日期" value-format="YYYY-MM-DD" size="large" /></el-form-item>
        <el-form-item><el-button type="primary" size="large" @click="search">搜索航班</el-button></el-form-item>
      </el-form>
    </div>
    <div class="city-tags">
      <el-tag v-for="c in cities" :key="c" class="tag" @click="form.departureCity = c; search()">{{ c }}</el-tag>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

const router = useRouter()
const form = ref({ departureCity: '', arrivalCity: '', date: '' })
const cities = ref([])

onMounted(async () => {
  try { const r = await api.get('/flight/departureCities'); cities.value = r.data } catch {}
})

function search() {
  if (!form.value.departureCity || !form.value.arrivalCity) return
  router.push({ path: '/flight/search', query: form.value })
}
</script>

<style scoped>
.hero { text-align: center; padding: 80px 20px 60px; background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%); color: #fff; }
.hero h1 { font-size: 36px; margin-bottom: 12px; }
.hero p { font-size: 18px; color: #a0aec0; margin-bottom: 40px; }
.search-box { background: #fff; padding: 24px; border-radius: 12px; display: inline-block; box-shadow: 0 8px 30px rgba(0,0,0,0.3); }
.city-tags { margin-top: 20px; }
.tag { cursor: pointer; margin: 4px; }
</style>
