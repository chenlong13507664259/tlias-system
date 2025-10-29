<script setup lang="ts">
import type { ClazzModel, StuModel } from '@/api/model/model';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { onMounted, ref } from 'vue';
import qs from 'qs';

let token=localStorage.getItem('token');

const searchStu = ref({
  name: '',
  degree: '',
  clazzId: ''
});

const clazzList = ref<ClazzModel[]>([]);
const stuList = ref<StuModel[]>([]);

const queryClasses = async () => {
  let allClasses: ClazzModel[] = [];  // 用来存储所有班级数据
  let currentPage = 1;  // 从第一页开始
  const pageSize = 5;   // 每页显示5条数据
  
  try {
    // 循环加载数据，直到加载完所有班级数据
    while (true) {
      const res = await axios.get('/api/clazzs', {
        params: {
          name: '',
          begin: '',
          end: '',
          page: currentPage,
          pageSize: pageSize
        }, headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
      });

      // 打印接口返回的数据，检查返回格式
      //console.log('clazzList接口返回：', res.data);

      if (res.data.code === 1 && Array.isArray(res.data.data.rows)) {
        // 合并当前页的数据到 allClasses 数组
        allClasses = [...allClasses, ...res.data.data.rows];

        // 判断是否加载完所有数据（根据 total 字段）
        if (allClasses.length >= res.data.data.total) {
          break;  // 如果加载的数据数量 >= 总数量，退出循环
        }

        // 否则加载下一页
        currentPage++;
      }
    }

    // 最终将所有班级数据赋值给 clazzList
    clazzList.value = allClasses;

  } catch (error) {
    console.error('获取班级数据失败:', error);
  }
};


const getClassName = async (id: any) => {
  try {
    const res = await axios.get('/api/clazzs/' + id,{ headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });
    // console.log('getClassName接口返回：', res.data.data);
    // console.log('getClassName接口返回name：', res.data.data.name);
    return res.data.data.name;
  } catch (error) {
    console.error('获取班级信息失败:', error);
    return '未知班级';
  }
};

const queryAll = async () => {
  try {
    const res = await axios.get('/api/students', {
      params: {
        name: searchStu.value.name,
        degree: searchStu.value.degree,
        clazzId: searchStu.value.clazzId,
        page: page.value,
        pageSize: pageSize.value,
      }, headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });

    if (res.data.code === 1 && Array.isArray(res.data.data.rows)) {
      stuList.value = res.data.data.rows;

      // 为每个学生的 clazzId 加载班级名称
      for (const student of stuList.value) {
        const className = await getClassName(student.clazzId);
        student.clazzName = className;  // 动态添加班级名称到学生数据
      }

      total.value = res.data.data.total;
    }
  } catch (error) {
    console.error("获取学生数据失败:", error);
  }
};

onMounted(async () => {
  await queryAll();
  await queryClasses();
});

const clean = () => {
  searchStu.value = {
    name: '',
    degree: '',
    clazzId: ''
  };
  queryAll();
};

const total = ref(0);
const page = ref(1);       // 当前页
const pageSize = ref(5);  // 每页显示数量

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

const stuSelect = ref<StuModel[]>([]);
const handleSelectionChange = (val: StuModel[]) => {
  stuSelect.value = val;
};

const stu=ref<StuModel>({
  id: '',
  name: '',
  no: '',
  gender: '',
  phone: '',
  idCard: '',
  isCollege: '',
  address: '',
  degree: '',
  graduationDate: '',
  clazzId: '',
  violationCount: 0,
  violationScore: 0,
  clazzName: ''
});

const dialogVisible = ref(false);
const formTitle = ref('');
const add = async () => {
  dialogVisible.value = true;
  formTitle.value = '新增学员';
  stu.value = { 
    id: '',
    name: '',
    no: '',
    gender: '',
    phone: '',
    idCard: '',
    isCollege: '',
    address: '',
    degree: '',
    graduationDate: '',
    clazzId: '',
    violationCount: 0,
    violationScore: 0,
    clazzName: ''
  }
};

const save = async () => {
  let res : any;
  if (!stu.value.id) { 
     res = await axios.post('/api/students', stu.value,{
       headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
     });
    // console.log("新增返回数据：",res.data);
  }else { 
    res = await axios.put('/api/students', stu.value,{
       headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });
    // console.log("修改返回数据：",res.data);
  }
   if (res.data.code === 200|| res.data.code === 1) {
      queryAll();
      ElMessage.success('保存成功');
      dialogVisible.value = false;
    } else {
      ElMessage.error('保存失败');
    }
};

const rules={
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '请输入编号', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { min: 11, max: 11, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号码', trigger: 'blur' },
    { min: 18, max: 18, message: '请输入正确的身份证号码', trigger: 'blur' }
  ],
  isCollege: [
    { required: true, message: '请选择学历', trigger: 'change' }
  ]
}


// 批量删除学生
const deleteByIds = async () => {
  if (stuSelect.value.length === 0) {
    ElMessage.warning('请先选择要删除的学生');
    return;
  }

  try {
    await ElMessageBox.confirm(
      `确认删除这 ${stuSelect.value.length} 条学生吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );

    // 获取选中的学生id，并拼成逗号分隔字符串
    const ids = stuSelect.value.map(item => item.id).join(',');
    console.log('待删除 ID:', ids);

    // 调用后端删除接口（路径参数方式）
    const res = await axios.delete(`/api/students/${ids}`,{
       headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });

    if (res.data.code === 1 || res.data.code === 200) {
      ElMessage.success('批量删除成功');
      queryAll();            // 重新拉数据
      stuSelect.value = [];  // 清空选中
    } else {
      ElMessage.error(res.data.msg || '删除失败');
    }
  } catch (err) {
    if (err !== 'cancel' && err !== 'close') {
      ElMessage.error('删除失败');
      console.error(err);
    }
  }
};



const update = async (id: any) => {
  dialogVisible.value = true;
  formTitle.value = '修改学员';
  //回显
  const res = await axios.get('/api/students/' + id,{
     headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
  });
  stu.value = res.data.data;
};

const deleteById = async (id: any) => {
  //是否确认删除弹窗
   await ElMessageBox.confirm(
      '确认删除？',
      '删除',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
  const res = await axios.delete('/api/students/' + id,{
     headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
  });
  if (res.data.code !== 1 && res.data.code !== 200) {
    ElMessage.error('删除失败');
    return;
  }else {
    ElMessage.success('删除成功');
    queryAll();
  }
};
</script>


<template>
<h1>学员管理</h1>
  <!-- 搜索栏 -->
   <!-- {{ searchStu }} -->
  <el-form :inline="true" :model="searchStu" class="demo-form-inline">

    <el-form-item label="姓名">
      <el-input v-model="searchStu.name" placeholder="请输入姓名"/>
    </el-form-item>
    <!-- 学历选择 -->
     <el-form-item label="最高学历">
      <el-select v-model="searchStu.degree" placeholder="请选择学历">
        <el-option label="初中" value="1"></el-option>
        <el-option label="高中" value="2"></el-option>
        <el-option label="大专" value="3"></el-option>
        <el-option label="本科" value="4"></el-option>
        <el-option label="硕士" value="5"></el-option>
        <el-option label="博士" value="6"></el-option>
      </el-select>
    </el-form-item>

     <el-form-item label="所属班级">
      <el-select v-model="searchStu.clazzId" placeholder="请选择班级">
    <el-option
      v-for="clazz in clazzList"
      :key="clazz.id"
      :label="clazz.name"
      :value="clazz.id"
    ></el-option>
  </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="queryAll">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>

  <el-button type="success" style="float: left" @click="add">+新增学员</el-button>
  <el-button type="danger" style="float: left" @click="deleteByIds">-批量删除</el-button>
  <br /><br />



<!-- 列表展示 -->
  <el-table :data="stuList" border style="width: 100%"  @selection-change="handleSelectionChange" >
    <!-- 多选列 -->
    <el-table-column type="selection" width="55"></el-table-column>

    <el-table-column prop="name" label="姓名" align="center" width="100px" />
    <el-table-column prop="no" label="学号" align="center" width="100px" />
    <el-table-column prop="clazzId" label="班级" align="center" width="100px" >
       <template #default="scope">
        {{ scope.row.clazzName || '加载中...' }}  <!-- 使用动态更新的 clazzName -->
      </template>
    </el-table-column>
    <el-table-column prop="gender" label="性别" align="center" width="90px">
      <template #default="scope">
      {{ scope.row.gender == '1' ? '男' : '女' }}
      </template>
    </el-table-column>
     <el-table-column prop="phone" label="手机号" align="center" width="100px" />

    <el-table-column prop="dggree" label="最高学历" align="center">
      <template #default="scope">
      {{
         scope.row.degree == '1' ? '初中' :
         scope.row.degree == '2' ? '高中' : 
         scope.row.degree == '3' ? '大专' : 
         scope.row.degree == '4' ? '本科' : 
         scope.row.degree == '5' ? '硕士' :  
         '博士' 
         }}
      </template>
      </el-table-column>
     <el-table-column prop="violationCount" label="违纪次数" align="center" width="100px" />
     <el-table-column prop="violationScore" label="违纪扣分" align="center" width="100px" />
    <el-table-column prop="updateTime" label="最后修改时间" align="center" />

    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="update(scope.row.id)" style="margin-right: 0px;">编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)" style="margin-left: 0px;">删除</el-button>
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

<!-- 新增||修改弹窗 -->
<el-dialog v-model="dialogVisible" :title="formTitle" width="50%">
  <!-- {{ stu }} -->
  <el-form ref="formRef" :model="stu" label-width="100px"  :rules="rules">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="stu.name" placeholder="请输入姓名" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="学号" prop="no">
          <el-input v-model="stu.no" placeholder="请输入学号" />
        </el-form-item>
      </el-col>
    </el-row>

     <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="性别" prop="gender">
          <el-select v-model="stu.gender" placeholder="请选择性别" >
          <el-option label="男" value="1" />
          <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="手机号"  prop="phone">
          <el-input v-model="stu.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-col>
      </el-row>

      <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="stu.idCard" placeholder="请输入身份证号" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否院校" prop="isCollege">
          <el-select v-model="stu.isCollege" placeholder="请选择" >
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="联系地址"  prop="address">
          <el-input v-model="stu.address" placeholder="请输入联系地址" />
        </el-form-item>
      </el-col>
      <el-col :span="12"> 
      <el-form-item label="最高学历" prop="degree">
        <el-select v-model="stu.degree" placeholder="请选择" >
        <el-option label="初中" value="1"></el-option>
        <el-option label="高中" value="2"></el-option>
        <el-option label="大专" value="3"></el-option>
        <el-option label="本科" value="4"></el-option>
        <el-option label="硕士" value="5"></el-option>
        <el-option label="博士" value="6"></el-option>
        </el-select>
        </el-form-item>
      </el-col>
    </el-row>


      <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="毕业时间">
          <el-date-picker v-model="stu.graduationDate" placeholder="请选择毕业时间" />
        </el-form-item>
      </el-col>
        <el-col :span="12">
        <el-form-item label="所属班级">
          <el-select v-model="stu.clazzId" placeholder="请选择" >
           <el-option
           v-for="clazz in clazzList"
           :key="clazz.id"
           :label="clazz.name"
          :value="clazz.id"
          ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
        <el-button @click="dialogVisible = false">取消</el-button>
      </el-form-item>
  </el-form>

</el-dialog>


</template>

<style scoped>

</style>