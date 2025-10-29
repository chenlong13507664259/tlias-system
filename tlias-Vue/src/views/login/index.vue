<script setup lang="ts"> 
import router from '@/router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

interface loginForm {
  username: string
  password: string
}
const loginForm = ref<loginForm>({
  username: '',
  password: ''
})

const resetForm = () => {
  loginFormRef.value.resetFields()
}



const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
  ]
})

const loginFormRef = ref()

const login = async () => {
  loginFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        const res = await axios.post('/api/login', loginForm.value)
        if (res.data.code === 200 || res.data.code === 1) {
          // 1. 保存 token
          localStorage.setItem('token', res.data.data.token || 'demo-token')
          localStorage.setItem('username', res.data.data.username)
          const username = localStorage.getItem('username')

          // 2. 提示登录成功
          ElMessage.success('登录成功')

          // 3. 跳转首页
          router.push('/index')
        } else {
          ElMessage.error('登录失败,用户名或密码错误！')
        }
      } catch (error) {
        ElMessage.error('登录接口异常，请稍后重试')
      }
    } else {
      ElMessage.warning('请完整填写登录信息')
    }
  })
}




</script>
<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">Tlias智能学习辅助系统</h2>
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>

.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('../../assets/R-C.jpg') no-repeat center center;
  background-size: cover; /* 背景图等比放大铺满 */
}


.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-weight: bold;
}
</style>