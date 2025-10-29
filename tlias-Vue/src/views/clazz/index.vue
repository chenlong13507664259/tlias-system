<script setup lang="ts">
import { ref, watch } from 'vue';
import axios from 'axios';
import type { ClazzModel, SearchClazzModel } from '@/api/model/model';
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus';

const clazzFormRef = ref<FormInstance>();

const searchClazz = ref<SearchClazzModel>({
  name: '',
  begin: '',
  end: '',
  date: []
});

const clazzList = ref<ClazzModel[]>([]);
const total = ref(0);
const page = ref(1);       // 当前页
const pageSize = ref(5);  // 每页显示数量

// 清空搜索条件
const clean = () => {
  searchClazz.value = {
    name: '',
    begin: '',
    end: '',
    date: []
  };
  page.value = 1;
  queryAll();
};
let token = localStorage.getItem('token');  // 从 localStorage 获取 token
// 查询接口
const queryAll = async () => {
  // const token = localStorage.getItem('token');  // 从 localStorage 获取 token
  // console.log('当前token:', token);
  // console.log('请求头token:', `${token}`);
  const res = await axios.get('/api/clazzs', {
    params: {
      name: searchClazz.value.name,
      begin: searchClazz.value.begin,
      end: searchClazz.value.end,
      page: page.value,
      pageSize: pageSize.value,
      token: token
    },headers: {
       'token': `${token}`,  // 将 token 放在 Authorization 头部
       'Content-Type': 'application/json'
      }
  });
  // console.log('接口返回：', res.data);

  clazzList.value = res.data.data.rows;
  total.value = res.data.data.total;
};

//把data:[] 赋给begin 和 end
watch(() => searchClazz.value.date, (newValue) => { 
  searchClazz.value.begin = newValue[0];
  searchClazz.value.end = newValue[1];
});



// 新增、编辑、删除方法
const dialogVisible = ref(false);
const formTitle = ref('');
const clazz = ref<ClazzModel>({
  id: '',
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  subject: '',
  masterId: ''
});

const add = async() => {
  dialogVisible.value = true;
  formTitle.value = '新增班级';
  clazz.value = { 
    id: '',
    name: '',
    room: '',
    beginDate: '',
    endDate: '',
    subject: '',
    masterId: ''
  };
};

// 保存
const save = async () => { 
   clazzFormRef.value?.validate(async (valid) => {
    if (!valid) return; // 校验不通过，阻止提交
    let res: any;
    if(clazz.value.id){
      res = await axios.put('/api/clazzs', clazz.value,{headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }});
      console.log(res.data)
    }else{
   res = await axios.post('/api/clazzs', clazz.value,{
    headers: {
      'token': `${token}`,  // 将 token 放在头部
      'Content-Type': 'application/json'
    }
   });
   console.log(res.data)
    }
   if (res.data.code === 200|| res.data.code === 1) {
    searchClazz.value = { name: '', begin: '', end: '', date: [] };
    page.value = 1;
    queryAll()
    dialogVisible.value = false
    ElMessage.success('保存成功！');
  }else{
    ElMessage.error("保存失败！")
  }
});
};

// 修改回显
const update = async(id:number) => {
  dialogVisible.value = true;
  formTitle.value = '修改班级';
  clazz.value = { 
    id: '',
    name: '',
    room: '',
    beginDate: '',
    endDate: '',
    subject: '',
    masterId: ''
  };
  const res = await axios.get('/api/clazzs/' + id,{
    headers: {
       'token': `${token}`,  // 将 token 放在 Authorization 头部
       'Content-Type': 'application/json'
      }
  });
  clazz.value = res.data.data;
};


const deleteById = async(id: number) => {
  try {
    
    //是否确认删除弹窗
   await ElMessageBox.confirm(
      '确认删除？',
      '删除班级',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    const res = await axios.delete('/api/clazzs/' + id,{
      headers: {
       'token': `${token}`,  // 将 token 放在 Authorization 头部
       'Content-Type': 'application/json'
      }
    });
    if (res.data.code !== 1 && res.data.code !== 200) {
      ElMessage.error('删除失败');
      return;
    }
    ElMessage.success('删除成功');
    queryAll();
  } catch (error) {
    console.error('删除失败:', error);
  }
};

// 页码改变
const handlePageChange = (newPage: number) => {
  page.value = newPage;
  queryAll();
};

// 每页条数改变
const handlePageSizeChange = (newSize: number) => {
  pageSize.value = newSize;
  page.value = 1;
  queryAll();
};

// 页面初始化查询一次
queryAll();


const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'change' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }]
};
</script>

<template>
  <!-- {{searchClazz}} -->
  <h1>班级管理</h1>

  <!-- 搜索栏 -->
  <el-form :inline="true" :model="searchClazz" class="demo-form-inline">
    <el-form-item label="班级名称">
      <el-input v-model="searchClazz.name" placeholder="请输入名称"/>
    </el-form-item>

    <el-form-item label="结课时间"> 
      <el-date-picker
        v-model="searchClazz.date"
        type="daterange"
        range-separator="到"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="queryAll">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>

  <el-button type="success" style="float: left" @click="add">+新增班级</el-button>
  <br /><br />

  <!-- 表格 -->
  <el-table :data="clazzList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="60" align="center">
      <template #default="scope">
    {{ (page - 1) * pageSize + scope.$index + 1 }}
      </template>
    </el-table-column>
    <el-table-column prop="name" label="班级名称" width="100" align="center"/>
    <el-table-column prop="room" label="班级教室" width="100" align="center"/>
    <el-table-column prop="masterId" label="班主任" width="100" align="center">
       <template #default="scope">
       {{scope.row.masterId=='1'? '李老师':
         scope.row.masterId=='2'? '陈老师':
         scope.row.masterId=='3'? '刘老师':
         scope.row.masterId=='4'? '张老师': '王老师'
        }}
       </template>
    </el-table-column>

    <el-table-column prop="subject" label="学科" width="100" align="center">
       <template #default="scope">
       {{scope.row.subject=='1'? 'java':
         scope.row.subject=='2'? 'python':
         scope.row.subject=='3'? 'C++': 'c#'
        }}
       </template>
    </el-table-column>

    <el-table-column prop="beginDate" label="开课时间" width="120" align="center"/>
    <el-table-column prop="endDate" label="结课时间" width="120" align="center"/>
    <el-table-column prop="updateTime" label="最后修改时间" width="160" align="center" />
    <el-table-column label="操作" width="150" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="update(scope.row.id)">编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页 -->
  <el-pagination
    style="margin-top: 20px; text-align: right;"
    background
    layout="total, sizes, prev, pager, next, jumper"
    :total="total"
    :page-size="pageSize"
    :current-page="page"
    @size-change="handlePageSizeChange"
    @current-change="handlePageChange"
  />

  <!-- 新增||修改窗口 -->
   <el-dialog v-model="dialogVisible" :title="formTitle" width="40%">
    <!-- {{ clazz }} -->
    <el-form ref="clazzFormRef" :model="clazz" :rules="rules" label-width="80px">
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="clazz.name" placeholder="请输入班级名称"/>
      </el-form-item>
      <el-form-item label="班级教室" prop="room">
        <el-input v-model="clazz.room" placeholder="请输入教室名称" />
      </el-form-item>
      <!--开课时间 -->
      <el-form-item label="开课时间" prop="beginDate"> 
        <el-date-picker
          v-model="clazz.beginDate"
          type="date"
          placeholder="请选择日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="结课时间" prop="endDate"> 
        <el-date-picker
          v-model="clazz.endDate"
          type="date"
          placeholder="请选择日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="班主任" prop="masterId"> 
        <el-select v-model="clazz.masterId" placeholder="请选择">
          <el-option label="李老师" :value="1"></el-option>
          <el-option label="陈老师" :value="2"></el-option>
          <el-option label="刘老师" :value="3"></el-option>
          <el-option label="张老师" :value="4"></el-option>
          <el-option label="王老师" :value="5"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学科" prop="subject"> 
        <el-select v-model="clazz.subject" placeholder="请选择">
          <el-option label="java" :value="1"></el-option>
          <el-option label="python" :value="2"></el-option>
          <el-option label="c++" :value="3"></el-option>
          <el-option label="c#" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
   </el-dialog>



</template>


<style scoped>

</style>