package com.example.Controller;

import com.example.Entity.Clazz;
import com.example.Entity.ClazzPages;
import com.example.Entity.Result;
import com.example.Service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    /*
    * 获取班级列表(分页查询)
     */
    @GetMapping("/clazzs")
    public Result list(@RequestParam String name,
                       @RequestParam(defaultValue = "") LocalDate begin,
                      @RequestParam(defaultValue = "") LocalDate end,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "5") Integer pageSize)
    {
        ClazzPages clazzPages = clazzService.list(name, begin, end, page, pageSize);
        return Result.success(clazzPages);
    }

    /*
    * 删除班级
     */
    @DeleteMapping("/clazzs/{id}")
    public Result delete(@PathVariable Integer id)
    {
        clazzService.delete(id);
        return Result.success();
    }

    /*
    * 添加班级
     */
    @PostMapping("/clazzs")
    public Result add(@RequestBody Clazz clazz)
    {
        clazzService.add(clazz);
        return Result.success();

    }

    /*
    * 根据id查询班级
     */
    @GetMapping("/clazzs/{id}")
    public Result get(@PathVariable Integer id)
    {
        Clazz clazz = clazzService.get(id);
        return Result.success(clazz);
    }

    /*
    * 修改班级
     */
    @PutMapping("/clazzs")
    public Result update(@RequestBody Clazz clazz)
        {
            clazzService.update(clazz);
            return Result.success();
        }


    /*
    * 获取所有班级
     */
    @GetMapping("/clazzs/list")
    public Result list()
    {
        List<Clazz> clazzs = clazzService.list();
        return Result.success(clazzs);
    }
}
