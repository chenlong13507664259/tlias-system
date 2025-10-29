package com.example.Controller;

import com.example.Entity.Dept;
import com.example.Entity.Result;
import com.example.Service.Impl.DeptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/depts")
public class DeptController   {

    @Autowired//从ioc容器中 自动寻找bean对象 为该变量赋值
    private DeptServiceImpl deptService;
    @GetMapping
    public Result list() throws Exception {
        //1 调用service 获取数据
        List<Dept> depts = deptService.list();
        //3 响应数据（json格式 ）
        return Result.success(depts);
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) throws Exception {
        //1 调用service 获取数据
        deptService.deleteById(id);
        //3 响应数据（json格式 ）
        return Result.success();
    }

    //新增
    @PostMapping
    public Result add(@RequestBody Dept dept) throws Exception {
        try {
            deptService.add(dept);
        }catch (Exception e) {
            return Result.error("此部门已存在！");
        }

        return Result.success();
    }

    //数据回显(查询数据)
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) throws Exception {
        return Result.success(deptService.findById(id));
    }
    //修改数据
    @PutMapping
    public Result update(@RequestBody Dept dept) throws Exception {
        deptService.update(dept);
        return Result.success();
    }
}