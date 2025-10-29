// src/api/emp.ts
import request from '@/utils/request'; // 根据你的 axios 实例路径调整
import type { EmpModelArray } from './model/model';

export interface GetEmpListParams {
  name?: string;
  gender?: string;
  begin?: string;
  end?: string;
  page: number;
  pageSize: number;
}

export const getEmpListApi = (params: GetEmpListParams) => {
  return request.get<{ rows: EmpModelArray; total: number }>('/emps', { params });
};