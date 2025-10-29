<script setup lang="ts">
import { useRouter } from 'vue-router'

// 从 localStorage 获取当前用户的信息（假设存储在 userInfo 中）
const username = localStorage.getItem('username')
// console.log('当前用户信息：', username)

const router = useRouter()

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/login')
}

const changePassword = () => {
  router.push('/change-password')
}

</script>


<template>
    <div class="common-layout">
        <el-container>
            <!-- 顶栏 --->
          <el-header class="header">
  <span class="header-left">智能学习辅助系统</span>
  <span class="header-right">
    <a class="logout" href="javascript:void(0)" @click="logout">
      退出登录
      <el-icon><SwitchButton /></el-icon>
    </a>
    <a href="javascript:void(0)" @click="changePassword">
      修改密码
      <el-icon><EditPen /></el-icon>
    </a>
    <!-- 显示当前用户的名字 -->
    <span class="username" v-if="username">当前用户[{{username}}]</span>
  </span>
</el-header>



            <el-container>
                <!-- 左侧 --->
                <el-aside width="200px">
                    <el-scrollbar>

                        <!-- 菜单加入router路由 -->
                        <el-menu router>
                        
                            <el-menu-item index="/index">
                               <el-icon><Promotion /></el-icon>首页
                            </el-menu-item>
                       
                            <el-sub-menu index="1">
                                <template #title>
                                    <el-icon>
                                        <Menu />
                                    </el-icon>班级学员管理
                                </template>
                               
                                <el-menu-item-group>
                                   
                                    <el-menu-item index="/clazz"><el-icon>
                                            <HomeFilled />
                                        </el-icon>班级管理</el-menu-item>
                                       
                                       
                                    <el-menu-item index="/stu"><el-icon>
                                            <UserFilled />
                                        </el-icon>学员管理</el-menu-item>
                                  
                                </el-menu-item-group>
                              
                            </el-sub-menu>


                            <el-sub-menu index="2">
                                <template #title>
                                    <el-icon><Tools /></el-icon>系统信息管理
                                </template>
                                <el-menu-item-group>

                                  
                                    <el-menu-item index="/dept"><el-icon><HelpFilled /></el-icon>部门管理</el-menu-item>
                                  

                                    <el-menu-item index="/emp"><el-icon>
                                            <UserFilled />
                                        </el-icon>员工管理</el-menu-item>
                                 

                                </el-menu-item-group>
                            </el-sub-menu>


                              <el-sub-menu index="3">
                                <template #title>
                                   <el-icon><Histogram /></el-icon>数据统计管理
                                </template>
                                <el-menu-item-group>
                                    <el-menu-item index="3-1"><el-icon><InfoFilled /></el-icon>员工信息统计</el-menu-item>
                                    <el-menu-item index="3-2"><el-icon><Share /></el-icon>学员信息统计</el-menu-item>
                                    <el-menu-item index="3-3"><el-icon><Document /></el-icon>日志信息统计</el-menu-item>
                                </el-menu-item-group>
                            </el-sub-menu>

                        </el-menu>
                    </el-scrollbar>
                </el-aside>
                <!-- 中间 --->
                <el-main>
                    <router-view> </router-view>
                     
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
.header {
    background-color: cornflowerblue;
}

.username {
    color: #fff;
    float: right;
    margin-left:0px;
    /*文字居左*/
    line-height: 60px;
    
}


.header-left {
    color: #fff;
    text-align: left;
    /*文字居左*/
    line-height: 60px;
    font-size: 30px;
    /*字体大小*/
}

.header-right a {
    /*带a标签*/
    float: right;
    /*右浮动*/
    line-height: 60px;
    color: white;  
    /*去掉下划线*/
    text-decoration: none;
    /*文字间距*/
    margin-left: 10px;
   
}

</style>