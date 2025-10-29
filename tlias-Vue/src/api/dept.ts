//封装ts代码-请求后端业务
//导入请求方法
import request from '@/utils/request'
import type { DeptModel, ResultModel } from '@/api/model/model'

//查询所有部门
export const queryAllApi=()=>request.get<any,ResultModel>('/depts')
