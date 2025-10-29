<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { DeptModel } from '@/api/model/model'
import axios from 'axios'
import { ElMessage,ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { queryAllApi } from '@/api/dept'


const deptList = ref<DeptModel[]>([])

const queryAll = async () => {
  //法一直接获取数据信息'
  // const res = await axios.get('http://localhost:8080/depts')
  // deptList.value = res.data.data; 

//法二 通过api文件下的ts文件内queryAllApi函数获取数据信息,再导入本文件,再const res = await queryAllApi();
const res = await queryAllApi();
deptList.value = res.data;
}

// 页面加载时获取数据
onMounted(() => {
  queryAll()
})


//新增按钮点击事件
const dialogFormVisible = ref(false)

const add = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增部门';
}

const deptForm = ref<DeptModel>({
  id: undefined,
  name: ''
})

let formTitle = ref<String>('')

const formLabelWidth = '120px'

let token=localStorage.getItem('token')
//保存按钮点击事件
const save = async (formEl: FormInstance|undefined) => {
  // 提交数据之前校验
  if (!formEl) return;

  try {
    await formEl.validate(); // ✅ 验证成功后才继续
  } catch (fields) {
    console.log('表单校验失败:', fields);
    ElMessage.error('请填写正确的信息');
    return; // ❗ 验证失败，直接 return，阻止继续保存
  }
  console.log('deptForm.value:', deptForm.value)
  if (!deptForm.value.id) {
    // 新增逻辑
    try {
    const res = await axios.post('/api/depts', deptForm.value,{
      headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    })
      if (res.data.code == 200||res.data.code == 1) {
         ElMessage.success('添加成功') // ✅ 添加成功提示
        queryAll()
        dialogFormVisible.value = false
        deptForm.value = { id: undefined, name: '' }
      }
    } catch(e) {
      ElMessage("保存失败")
    }
  } else {
    // 修改逻辑
    try {
      const saveData = {
      id: deptForm.value.id,
      name: deptForm.value.name
      }
      const res = await axios.put('/api/depts', saveData, {
        headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
      })
      console.log('修改接口返回：', res.data)
      if (res.data.code === 200||res.data.code === 1) {
         ElMessage.success('修改成功') // ✅ 添加成功提示
        queryAll()
        dialogFormVisible.value = false
        deptForm.value = { id: undefined, name: '' }
      } else {
        ElMessage('保存失败：' + res.data.msg)
      }
    } catch (e: unknown) {
  if (e && typeof e === 'object' && 'response' in e) {
    const err = e as { response: { data: { msg?: string } } }
    console.error('服务器返回错误:', err.response.data)
    ElMessage('保存失败：' + (err.response.data.msg || '未知错误'))
  } else {
    console.error('请求错误:', e)
    ElMessage('保存失败，请联系管理员')
  }

    }
  }
}


//修改
const update = async(id: number) => { 
  dialogFormVisible.value = true;
  formTitle.value = '修改部门';
  
  const res = await axios.get('/api/depts/'+id,{
    headers: {
       'token': `${token}`,  // 将 token 放在 Authorization 头部
       'Content-Type': 'application/json'
      }
  })
  deptForm.value = res.data.data;//赋值给deptForm(回显)
   console.log(res.data) 
   
}

//删除
const deleteById = async(id: number) => { 
   ElMessageBox.confirm(
    '确认删除？',
    '删除部门',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {//点击确认按钮后执行
const res = await axios.delete('/api/depts?id='+id,{
  headers: {
       'token': `${token}`,  // 将 token 放在 Authorization 头部
       'Content-Type': 'application/json'
      }
})
     
if(res.data.code === 200||res.data.code === 1) {
      queryAll()
       ElMessage({
        type: 'success',
        message: '删除成功！',
      })
      
    } else {
      ElMessage.error('删除失败')
    }

     
    })
    .catch(() => {//点击取消按钮后执行
      ElMessage({
        type: 'info',
        message: '删除取消！',
      })
    })
}

//定义校验规则
const deptFormRef=ref<FormInstance>()
const rules =ref<FormRules<DeptModel>>({
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ]
})




</script>

<template>
  <h1>部门管理</h1>
 <el-button type="primary" style="float: right" @click="add">+新增</el-button>
 
 


 <br>
 <br>

  <el-table :data="deptList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100"  align="center" />
    <el-table-column prop="name" label="部门名称" width="300"  align="center"/>
    <el-table-column prop="updateTime" label="最后修改时间" width="300"  align="center"/>
    <el-table-column label="操作" width="300"  align="center">  
       <template #default="scope">
        <el-button type="primary" size="small" @click="update(scope.row.id)">编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
      </el-table-column>
  </el-table>

  <!-- 新增||修改对话框 -->
   <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500"> <!-- :title绑定formTitle变量，不带:则无法显示标题 -->
    <el-form :model="deptForm" :rules="rules" ref="deptFormRef" label-width="120px">
      <el-form-item label="部门名称" :label-width="formLabelWidth" prop="name">
        <el-input v-model="deptForm.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save(deptFormRef)">
          保存
        </el-button>
      </div>
    </template>
  </el-dialog>

  </template>

<style scoped>

</style>
