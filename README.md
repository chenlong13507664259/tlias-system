# Tlias管理系统

Tlias管理系统是一个基于前后端分离架构的管理系统，前端使用Vue 3 + Vite技术栈，后端采用Spring Boot框架开发。

## 技术栈

### 前端技术栈
- Vue 3 (Composition API)
- Vite 构建工具
- TypeScript
- Vue Router 路由管理
- Pinia 状态管理
- Element Plus UI组件库
- Axios HTTP客户端
- ECharts 图表库

### 后端技术栈
- Spring Boot 3.5.3
- MyBatis 数据持久层框架
- MySQL 数据库
- JWT Token 认证
- Lombok 简化Java代码
- 阿里云OSS SDK

## 功能模块

### 前端功能页面
1. 登录页面 (login)
2. 系统主页布局 (layout)
3. 员工管理 (emp)
4. 部门管理 (dept)
5. 班级管理 (clazz)
6. 学员管理 (stu)
7. 首页仪表板 (index)

### 后端功能模块
1. Controller 控制层
2. Service 业务层
3. Mapper 数据访问层
4. Entity 实体类
5. Utils 工具类
6. Filter 过滤器
7. Interceptor 拦截器

## 环境要求

### 前端环境
- Node.js >= 16.0
- npm >= 8.0

### 后端环境
- Java 21
- Maven 3.6+
- MySQL 8.0+


## 项目特点

- 前后端完全分离，便于维护和扩展
- 使用JWT实现用户身份认证和权限控制
- 基于Element Plus的现代化UI界面
- RESTful API设计风格
- 完整的CRUD操作示例
- 支持文件上传至阿里云OSS

## 开发工具推荐

### 前端开发
- VSCode + Volar插件
- Chrome浏览器调试工具

### 后端开发
- IntelliJ IDEA
- Postman (API测试)

## 注意事项

1. 启动前端前需要确保后端服务已启动
2. 根据实际环境修改数据库连接配置
3. 如需使用阿里云OSS功能，需要配置相关密钥信息


## 快速开始

### 前端启动步骤
1. 进入前端项目目录
2. 安装依赖 npm install
3. 启动开发服务器 npm run dev

   
### 后端启动步骤
1. 创建数据库并导入SQL脚本
2. 修改application.yml配置文件中的数据库连接信息
3. 使用Maven构建项目 mvn clean install
4. 启动Spring Boot应用 mvn spring-boot:run
   
   
   
   

