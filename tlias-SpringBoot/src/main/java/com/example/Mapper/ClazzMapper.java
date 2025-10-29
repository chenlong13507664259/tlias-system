package com.example.Mapper;

import com.example.Entity.Clazz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClazzMapper {

////分页查询
//    @Select("select * from tlias.clazz where name=#{name} and begin_date>=#{begin_date}&&end_date<=#{end_date} limit #{start},#{pageSize}")
//    public List<Clazz> ClazzPage(String name, LocalDate begin_date, LocalDate end_date,Integer start, Integer pageSize);
//    @Select("select count(*) from tlias.clazz")
//    Long count();

    // 使用动态 SQL 查询分页数据(xml)
    List<Clazz> ClazzPage(@Param("name") String name,
                          @Param("begin_date") LocalDate begin_date,
                          @Param("end_date") LocalDate end_date,
                          @Param("start") Integer start,
                          @Param("pageSize") Integer pageSize);

    // 查询总记录数（支持动态条件）
    Long count(@Param("name") String name,
               @Param("begin_date") LocalDate begin_date,
               @Param("end_date") LocalDate end_date);

    // 删除
    @Select("delete from tlias.clazz where id=#{id}")
    void delete(Integer id);

    // 添加
    @Select("insert into tlias.clazz(name,room,begin_date,end_date,master_id,subject,create_time,update_time) values(#{name},#{room},#{beginDate},#{endDate},#{masterId},#{subject},#{createTime},#{updateTime})")
    void add(Clazz clazz);

    // 根据id查询
    @Select("select * from tlias.clazz where id= #{id}")
    Clazz get(Integer id);

    // 修改
    @Select("update tlias.clazz set name=#{name},room=#{room},begin_date=#{beginDate},end_date=#{endDate},master_id=#{masterId},subject=#{subject},create_time=#{createTime},update_time=#{updateTime} where id=#{id}")
    void update(Clazz clazz);

    // 查询所有
    @Select("select * from tlias.clazz")
    List<Clazz> list();
}
