package com.example.Mapper;

import com.example.Entity.Emp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpMapper {

    /**
     * 查询总记录数
     * @return
     * @throws Exception
     */
//    @Select("select count(*) from emp")
    public Long count(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("begin") String begin,
            @Param("end") String end
    ) throws Exception;

    /**
     * 分页查询
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<Emp> page(
            @Param("start") Integer start,
            @Param("pageSize") Integer pageSize,
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("begin") String begin,
            @Param("end") String end
    ) throws Exception;

    /**
     * 添加员工基本信息
     * @param emp
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")//获取主键的值并赋给id属性
    @Insert("insert into emp(username,password,name,gender,phone,job,salary,image,entry_date,dept_id,create_time,update_time) values(#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    /**
     * 批量删除员工信息
     *
     * @param ids
     */
    //xml文件方式
    void deleteBatch(List<Integer> ids) throws Exception;

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    //已经使用xml文件方式
  //@Select("select * from emp e left join tlias.emp_expr ee on e.id=ee.emp_id where e.id = #{id}")
    Emp getById(Integer id);

    @Select("select * from emp where id = #{id}")
    Emp getById2(Integer id);

    /**
     * 修改员工信息
     * @param emp
     */
    //动态 sql
    void update(Emp emp);

    /*
    * 根据用户名和密码查询员工
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getByUsernameAndPassword(String username, String password);
}
