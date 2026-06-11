import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { auth: true } },
  { path: '/flight/search', name: 'FlightSearch', component: () => import('../views/FlightSearch.vue') },
  { path: '/flight/:id', name: 'FlightDetail', component: () => import('../views/FlightDetail.vue') },
  { path: '/booking/:flightId', name: 'Booking', component: () => import('../views/Booking.vue'), meta: { auth: true } },
  { path: '/booking/confirm/:orderNo', name: 'BookingConfirm', component: () => import('../views/BookingConfirm.vue'), meta: { auth: true } },
  { path: '/orders', name: 'OrderList', component: () => import('../views/OrderList.vue'), meta: { auth: true } },
  { path: '/order/:orderNo', name: 'OrderDetail', component: () => import('../views/OrderDetail.vue'), meta: { auth: true } },
  // Admin routes
  { path: '/admin', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/flights', name: 'AdminFlights', component: () => import('../views/admin/FlightManage.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/flights/add', name: 'AdminFlightAdd', component: () => import('../views/admin/FlightForm.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/flights/edit/:id', name: 'AdminFlightEdit', component: () => import('../views/admin/FlightForm.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/orders/:id', name: 'AdminOrderDetail', component: () => import('../views/admin/OrderDetail.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/users', name: 'AdminUsers', component: () => import('../views/admin/UserManage.vue'), meta: { auth: true, admin: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.auth && !token) {
    next('/login')
  } else if (to.meta.admin) {
    const role = localStorage.getItem('role')
    if (role !== 'ADMIN') {
      next('/')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
