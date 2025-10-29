package com.example.Mapper;

import com.example.Entity.EmpExpr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    /**
     * 批量插入
     *
     * @param exprList
     */

    //基于xml开发-动态sql--<foreach>
    void insertBatch(@Param("exprList") List<EmpExpr> exprList);

    /**
     * 批量删除
     *
     * @param empIds
     */
//基于xml开发-动态sql--<foreach>
    void deleteBatch(List<Integer> empIds);

    /**
     * 根据员工id查询员工表经历达式
     *
     * @paramempId
     * @return
     */
    @Select("select * from emp_expr where emp_id=#{empId}")
    List<EmpExpr> getByEmpId(Integer id);

    /**
     * 根据员工id删除员工表经历达式
     *
     * @paramempId
     */
    @Select("delete from emp_expr where emp_id= #{empId}")
    void deleteByEmpId(Integer id);
}
