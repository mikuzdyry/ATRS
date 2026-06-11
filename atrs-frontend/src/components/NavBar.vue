<template>
  <el-menu :default-active="route.path" mode="horizontal" router class="navbar">
    <el-menu-item index="/">
      <el-icon><Promotion /></el-icon>
      <span>ATRS 航空票务</span>
    </el-menu-item>
    <el-menu-item index="/flight/search">航班搜索</el-menu-item>

    <template v-if="userStore.token">
      <el-menu-item index="/orders">我的订单</el-menu-item>
      <el-menu-item index="/profile">个人中心</el-menu-item>
      <el-sub-menu v-if="userStore.role === 'ADMIN'" index="admin">
        <template #title>管理后台</template>
        <el-menu-item index="/admin">仪表盘</el-menu-item>
        <el-menu-item index="/admin/flights">航班管理</el-menu-item>
        <el-menu-item index="/admin/orders">订单管理</el-menu-item>
        <el-menu-item index="/admin/users">用户管理</el-menu-item>
      </el-sub-menu>
    </template>

    <div class="nav-right">
      <template v-if="userStore.token">
        <el-tag type="warning" v-if="userStore.role === 'ADMIN'">管理员</el-tag>
        <span class="username">{{ userStore.username }}</span>
        <el-button text @click="handleLogout">退出</el-button>
      </template>
      <template v-else>
        <el-button text @click="$router.push('/login')">登录</el-button>
        <el-button type="primary" size="small" @click="$router.push('/register')">注册</el-button>
      </template>
    </div>
  </el-menu>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.navbar { padding: 0 20px; }
.nav-right { margin-left: auto; display: flex; align-items: center; gap: 10px; }
.username { color: #606266; }
</style>
