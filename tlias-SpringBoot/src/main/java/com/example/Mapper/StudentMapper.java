package com.example.Mapper;

import com.example.Entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    /**
     * 根据条件查询学生总数
     */
    Long count(@Param("name") String name,
               @Param("degree") String degree,
               @Param("clazzId") String clazzId);

    /**
     * 分页查询学生列表
     */
    List<Student> studentPage(@Param("name") String name,
                              @Param("degree") String degree,
                              @Param("clazzId") String clazzId,
                              @Param("start") Integer start,
                              @Param("pageSize") Integer pageSize);

    /**
     * 删除学生
     */
    @Delete("delete from tlias.student where id=#{id}")
    void delete(int id);

    /**
     * 添加学生
     */
    @Insert("insert into tlias.student(name,no,gender,phone,id_card,is_college,address,degree,graduation_date,clazz_id,create_time,update_time)" +
            " values(#{name},#{no},#{gender},#{phone},#{idCard},#{isCollege},#{address},#{degree},#{graduationDate},#{clazzId},#{createTime},#{updateTime})")
    void add(Student student);

    /**
     * 根据id查询学生
     */
    @Select("select * from tlias.student where id= #{id}")
    Student get(Integer id);

    /**
     * 修改学生信息
     */
    @Update("update tlias.student set name= #{name},no= #{no},gender= #{gender},phone= #{phone},id_card= #{idCard},is_college= #{isCollege},address= #{address},degree= #{degree},graduation_date= #{graduationDate},clazz_id= #{clazzId},create_time=#{createTime}, update_time= #{updateTime},violation_count=#{violationCount},violation_score=#{violationScore} where id=#{id}")
    void update(Student student);
}
