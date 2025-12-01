import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'
import MainPage from '../components/MainPage.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/main', name: 'Main', component: MainPage, meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 简单路由守卫：基于 localStorage.username 判断是否已登录
router.beforeEach((to, from, next) => {
  const username = localStorage.getItem('username')
  if (to.meta.requiresAuth && !username) {
    next({ name: 'Login' })
  } else if (to.name === 'Login' && username) {
    next({ name: 'Main' })
  } else {
    next()
  }
})

export default router
