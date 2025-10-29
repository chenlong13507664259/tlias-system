import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'

// 创建路由实例并传递 `routes` 配置(嵌套路由)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/layout/index.vue'),
      redirect: '/index',//默认显示的页面
      children: [//嵌套在主页面内，用children:[]包裹
        {
          path: '/index',
          name: 'index',
          component: () => import('../views/index/index.vue')
        },
        {
          path: '/dept',
          name: 'dept',
          component: () => import('../views/dept/index.vue')
        },
        {
          path: '/emp',
          name: 'emp',
          component: () => import('../views/emp/index.vue')
        },
        {
          path: '/clazz',
          name: 'clazz',
          component: () => import('../views/clazz/index.vue')
        },
        {
          path: '/stu',
          name: 'stu',
          component: () => import('../views/stu/index.vue')
        }]
    },  
       { path: '/login',
      name: 'login',
      component: () => import('../views/login/index.vue'),
       }
  ]
})



router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path !== '/login' && !token) {
    // 未登录访问非登录页 -> 跳转登录
    next('/login')
    console.log('未登录访问非登录页 -> 跳转登录')
  } else if (to.path === '/login' && token) {
    // 已登录访问登录页 -> 跳转首页
    next('/index')
    console.log('已登录访问登录页 -> 跳转首页')
  } else {
    next()  // 放行
    // console.log('放行')
    // console.log("token:",token)
  }
})


export default router
