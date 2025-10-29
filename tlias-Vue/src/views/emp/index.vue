<script setup lang="ts">
import type { DeptModel, EmpExprModel, EmpModel, EmpModelArray, SearchEmpModel } from '@/api/model/model';
import { reactive, ref , watch} from 'vue'
import type { PaginationParam } from '../../api/model/model';
import { onMounted } from 'vue';
import { getEmpListApi } from '@/api/emp'; // 获取员工列表的 API
import axios from 'axios';
import { queryAllApi } from '@/api/dept';
import { ElMessage, ElMessageBox } from 'element-plus';
import qs from 'qs';


let token = localStorage.getItem('token');
const searchEmp = ref<SearchEmpModel>({
  name: '', //姓名
  gender: '', //性别
  begin: '', //开始时间
  end: '', //结束时间
  date: []
})


//侦听date 属性的变化并给begin和end赋值
watch(
  () => searchEmp.value.date,
  (newVal) => {
    if (Array.isArray(newVal) && newVal.length === 2) {
      searchEmp.value.begin = newVal[0] || '';
      searchEmp.value.end = newVal[1] || '';
    } else {
      searchEmp.value.begin = '';
      searchEmp.value.end = '';
    }
  }
);


const empList = ref<EmpModelArray>([])
const pagination= ref<PaginationParam>({
  currentPage: 1,
  pageSize: 5,
  total: 0
})

//每页记录数发生变时触发函数
const handleSizeChange = (pageSize: number) => {
  console.log('切换到页码:', pageSize);
  pagination.value.pageSize = pageSize;
  queryAll();
}

//当前页码发生变时触发函数
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page;
  queryAll();
}


const queryAll = async () => {
  const params = {
    ...searchEmp.value,
    page: pagination.value.currentPage,
    pageSize: pagination.value.pageSize
  };

  try {
    const res = await getEmpListApi(params);
    empList.value = res.data.rows;
    pagination.value.total = res.data.total;
    // console.log('接口返回:', res);
    // console.log('接口返回 total:', res.data.total);
    // console.log('当前数据条数:', res.data.rows.length);

  } catch (error) {
    console.error('查询员工失败:', error);
  }

  console.log('请求参数:', params);

};


onMounted(() => {
  queryAll();
});

const pageQuery = () => {
  queryAll();
}

//搜索框清空功能
const clean = async () => {
  searchEmp.value.name = '';
  searchEmp.value.gender = '';
  searchEmp.value.date = []; // 触发 watcher 自动清空 begin/end
  pagination.value.currentPage = 1;
  await pageQuery();
};


// 控制对话框是否可见
const dialogVisible = ref(false)

let formTitle = ref<string>('')
//新增按钮点击事件
const add =async () => { 
  dialogVisible.value = true;
  formTitle.value = '新增员工';
  empForm.value = {
    id: undefined,
    username: '',
    password: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    image: '',
    entryDate: '',
    deptId: '',
    exprList: []
  };
    try {
    const res = await queryAllApi();
    deptList.value = res.data || [];
  } catch (error) {
    console.error('获取部门列表失败', error);
  }
}

// 对话框关闭事件
const handleDialogClose = () => { 
  dialogVisible.value = false
}

const empForm = ref<EmpModel>({
  username: '',
  password: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  image: '',
  entryDate: '',
  deptId: '',
  exprList: [] as EmpExprModel[]
})

//上传图片
const handleAvatarSuccess = (response: any) => {
  // 假设后端返回 { url: '图片地址' }
  empForm.value.image = response.data
  ElMessage.success('上传成功')
}

// 上传前校验
const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
  }
  return isImage && isLt2M
}




const deptList = ref<DeptModel[]>([])
const addExperience = () => {
  empForm.value.exprList.push({
    exprDate: ['', ''], // 时间范围数组
    begin: '',  // 开始时间字符串
    end: '',    // 结束时间字符串
    company: '',
    job: ''
  });
}

const updateBeginEnd = (val: string[], index: number) => {
  if (val && val.length === 2) {
    empForm.value.exprList[index].begin = val[0];
    empForm.value.exprList[index].end = val[1];
  } else {
    empForm.value.exprList[index].begin = '';
    empForm.value.exprList[index].end = '';
  }
};

const removeExperience = (index: number) => {
  empForm.value.exprList.splice(index, 1);
};


const empFormRef = ref();
// 保存函数
const save = async () => {
  if (!empFormRef.value) return;

  // 先校验表单
  empFormRef.value.validate(async (valid: boolean) => {
    if (!valid) {
      ElMessage.error('请完善表单信息');
      return;
    }

    try {
      let res;
      if(!empForm.value.id){
        //新增员工
        console.log('新增员工，参数:', empForm.value);
       res = await axios.post('/api/emps', empForm.value,{
         headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
       });
      }else{ 
      //修改员工
       console.log('修改员工，参数:', empForm.value);
       res = await axios.put('/api/emps', empForm.value,{
         headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
       });
      }
       console.log('后端返回:', res.data);
      if (res.data.code === 200 || res.data.code === 1) {
         ElMessage.success(empForm.value.id ? '修改成功' : '添加成功');
        queryAll();
        dialogVisible.value = false;
        empForm.value = {
          id: undefined,
          username: '',
          password: '',
          name: '',
          gender: '',
          phone: '',
          job: '',
          salary: '',
          image: '',
          entryDate: '',
          deptId: '',
          exprList: []
        };
      } else {
        ElMessage.error('操作失败,用户名或手机号已存在！');
      }
    } catch (error: any) {
      ElMessage.error('请求失败，请稍后重试');
    }
  });
};



const cancel = () => { 
  dialogVisible.value = false
};

// 表单校验规则
const rules = reactive({
  username: [
    { required: true, message: '用户名为必填项', trigger: 'blur' }
  ],
   name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '性别为必填项', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '手机号为必填项', trigger: 'blur' }
  ]
})

// 修改按钮点击事件
const update = async (id: number) => { 
  try {
    // 1. 请求数据
    const res = await axios.get(`/api/emps/${id}`,{
       headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });

    if (res.data.code !== 1 && res.data.code !== 200) {
      ElMessage.error(res.data.msg || '获取员工信息失败');
      return;
    }

    const empData = res.data.data; // ✅ 取真正的员工数据

    // 2. 保留响应式，赋值给表单
    Object.assign(empForm.value, empData);

    // 3. 格式化工作经历的时间区间
    empForm.value.exprList = empData.exprList?.map((item: any) => ({
      ...item,
      exprDate: [item.begin, item.end] // element-plus daterange 格式
    })) || [];

    // 4. 显示弹窗
    dialogVisible.value = true;
    formTitle.value = '修改员工';

    // 5. 拉取部门列表
    try {
      const deptRes = await queryAllApi();
      deptList.value = deptRes.data || [];
    } catch (error) {
      console.error('获取部门列表失败', error);
    }
  } catch (error) {
    console.error('查询员工失败:', error);
    ElMessage.error('查询员工失败');
  }
};


// 单条数据删除按钮点击事件
const deleteById = async (id: number) => { 
  try {
    await ElMessageBox.confirm('确定要删除该员工吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    const res = await axios.delete(`/api/emps`,{
      params:{
        ids:id.toString()
      },headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
    });
    if (res.data.code !== 1 && res.data.code !== 200) {
      ElMessage.error(res.data.msg || '删除员工失败');
      return;
    }
    ElMessage.success('删除员工成功');
    queryAll();
  } catch (error) {
    console.error('删除员工失败:', error);
  }
};


const selectedRows = ref<EmpModel[]>([]); // 保存表格选中的多条数据
// 监听多选框变化，保存当前选中行数组
function handleSelectionChange(val:EmpModel[]) {
  selectedRows.value = val;
}
//批量删除
const deleteByIds = async () => { 
 if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的员工');
    return;
  }
  try {
    await ElMessageBox.confirm(
      `确认删除这 ${selectedRows.value.length} 条员工吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );

    // 获取所有选中员工的id
    const ids = selectedRows.value.map(item => item.id);

    // 调用后端删除接口，params里传ids数组，后端接收 List<Integer> ids
    const res = await axios.delete('/api/emps', { params: { ids },
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),headers: {
       'token': `${token}`,  // 将 token 放在头部
       'Content-Type': 'application/json'
      }
      //arrayFormat: 'repeat' 让数组变成 ids=61&ids=59
   });

    if (res.data.code === 1 || res.data.code === 200) {
      ElMessage.success('批量删除成功');
      queryAll();       // 重新拉取数据
      selectedRows.value = []; // 清空已选
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


</script>

<template>
 <h1>员工管理</h1>
 <!-- {{ searchEmp }} -->
 <!-- 搜索栏 -->
  <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchEmp.name" placeholder="请输入姓名"/>
    </el-form-item>

    <el-form-item label="性别">
      <el-select v-model="searchEmp.gender" placeholder="请选择">
        <el-option label="男" value="1" />
        <el-option label="女" value="2" />
      </el-select>
    </el-form-item>

    <el-form-item label="入职时间">
      <el-date-picker
        v-model="searchEmp.date"
        type="daterange"
        range-separator="到"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="pageQuery">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>

  <!-- 功能按钮 -->
  <el-button type="success" @click="add">+ 新增员工</el-button>
  <el-button type="danger" @click="deleteByIds">- 批量删除</el-button>
  <br><br>

  
  <!-- 列表展示 -->
  <el-table :data="empList" border style="width: 100%"  @selection-change="handleSelectionChange" >
    <!-- 多选列 -->
    <el-table-column type="selection" width="55"></el-table-column>

    <el-table-column prop="name" label="姓名" align="center" width="130px" />

    <el-table-column prop="gender" label="性别" align="center" width="100px">
      <template #default="scope">
      {{ scope.row.gender == '1' ? '男' : '女' }}
      </template>
    </el-table-column>
  

    <el-table-column label="头像" align="center" width="100">
      <template #default="scope">
       <img :src="scope.row.image" alt="头像" style="width: 60px; height: 60px; border-radius: 50%;" />
      </template>
    </el-table-column>

    <el-table-column prop="deptName" label="所属部门" align="center" />

    <el-table-column prop="job" label="职位" align="center" width="100px">
      <template #default="scope">
      {{ scope.row.job == '1' ? '部门经理' : scope.row.job == '2' ? '普通员工' : '实习生' }}
      </template>
    </el-table-column>

    <el-table-column prop="entryDate" label="入职时间" align="center" width="130px" />

    <el-table-column prop="updateTime" label="最后修改时间" align="center" />

    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="update(scope.row.id)" style="margin-right: 0px;">编辑</el-button>
        <el-button type="danger" size="small" @click="deleteById(scope.row.id)" style="margin-right: 11px;">删除</el-button>
      </template>
    </el-table-column>

  </el-table>
  <br>

  <!-- 分页组件Pagination -->
  <el-pagination
    v-model:current-page="pagination.currentPage"
    v-model:page-size="pagination.pageSize"
    :page-sizes="[5, 10, 20, 50, 100]"
    layout="total, sizes, prev, pager, next, jumper"
    :total="pagination.total"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />

  <!-- 新增||修改员工信息对话框 -->
   <el-dialog
    :title="formTitle"
    v-model="dialogVisible"
    width="50%"
    @close="handleDialogClose">

    <!-- 调试用 -->
    <!-- {{ empForm }} -->
   <el-form ref="empFormRef" :model="empForm" :rules="rules" label-width="80px">
  <el-row gutter="20">
    <el-col :span="12">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="empForm.username" placeholder="请输入用户名" />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="empForm.name" placeholder="请输入姓名" />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="性别" prop="gender">
        <el-select v-model="empForm.gender" placeholder="性别">
          <el-option label="男" value="1" />
          <el-option label="女" value="2" />
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="empForm.phone" placeholder="请输入手机号" />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="薪资" prop="salary">
        <el-input v-model="empForm.salary" placeholder="请输入薪资" />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="入职日期" prop="entryDate">
        <el-date-picker
          v-model="empForm.entryDate"
          type="date"
          placeholder="请选择入职日期"
          style="width: 100%"
        />
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="所属部门" prop="deptId">
        <el-select v-model="empForm.deptId" placeholder="请选择">
           <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id"/>
        </el-select>
      </el-form-item>
    </el-col>

    <el-col :span="12">
      <el-form-item label="职位" prop="job">
        <el-select v-model="empForm.job" placeholder="请选择">
          <el-option label="部门经理" value="1" />
          <el-option label="普通员工" value="2" />
          <el-option label="实习生" value="3" />
        </el-select>
      </el-form-item>
    </el-col>

    <!-- 头像 -->
     <el-col :span="12">
     <el-form-item label="头像" prop="image">
     <el-upload
      class="avatar-uploader"
      action="/api/upload"  
      name="file"       
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <img v-if="empForm.image" :src="empForm.image" class="avatar" />
      <el-icon v-else class="avatar-uploader-icon"><plus /></el-icon>
      </el-upload>
      </el-form-item>
      </el-col>


     <!-- 工作经历动态列表 -->
      <el-col :span="24" style="margin-top: 10px;">
        <label style="font-weight: 600; display: block; margin-bottom: 8px;">
          工作经历
        </label>
        <div
          v-for="(expr, index) in empForm.exprList"
          :key="index"
          style="margin-bottom: 10px; border: 1px solid #eee; padding: 10px; border-radius: 4px;"
        >
          <el-row :gutter="10" align="middle">
            <el-col :span="10">
              <el-date-picker
                v-model="expr.exprDate"
                type="daterange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="YYYY-MM-DD"
                @change="(val: string[]) => updateBeginEnd(val, index)"
                style="width: 100%"
              />
            </el-col>
            <el-col :span="7">
              <el-input v-model="expr.company" placeholder="公司" />
            </el-col>
            <el-col :span="5">
              <el-input v-model="expr.job" placeholder="职位" />
            </el-col>
            <el-col :span="2">
             <el-button type="danger" size="small" @click="removeExperience(index)">
              删除
             </el-button>
            </el-col>
          </el-row>
        </div>
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          @click="addExperience"
        >
          添加工作经历
        </el-button>
      </el-col>

    <el-col :span="24" style="text-align: right;">
      <el-button type="primary" @click="save">保存</el-button>
      <el-button  @click="cancel">取消</el-button>
    </el-col>
  </el-row>
</el-form>

  </el-dialog>

</template>

<style scoped>
.avatar-uploader {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  position: relative; /* 让内部绝对定位元素参考 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader .el-upload {
  position: relative;
  width: 100%;
  height: 100%;
  display: block;
}

/* 关键：让 input 全覆盖，且透明可点 */
.avatar-uploader .el-upload input[type="file"] {
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
  opacity: 0 !important;
  cursor: pointer;
  z-index: 10;
  margin: 0 !important; /* 清除默认 margin/padding */
  padding: 0 !important;
}

/* 加号图标居中，且不阻挡点击 */
.avatar-uploader-icon {
  font-size: 38px;
  color: #8c939d;
  pointer-events: none;
  user-select: none;
  z-index: 5;
  position: relative;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  user-select: none;
  pointer-events: none;
  z-index: 5;
  position: relative;
}

</style>