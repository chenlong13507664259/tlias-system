package com.example.Controller;

import com.example.Entity.Emp;
import com.example.Entity.Result;
import com.example.Service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class EmpController {

    @Autowired
    EmpService empService;



    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */

    @GetMapping("/emps")
    public Result page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam String name,
            @RequestParam String gender,
            @RequestParam String begin,
            @RequestParam String end
    ) throws Exception {
        return Result.success(empService.page(page, pageSize,name, gender, begin, end));
    }

    @PostMapping("/emps")
    public Result add(@RequestBody Emp emp) throws Exception {
        empService.add(emp);
        return Result.success();
    }

    @DeleteMapping("/emps")
    public Result delete(@RequestParam List<Integer> ids) throws Exception {
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/emps/{id}")
    public Result getById(@PathVariable Integer id) throws Exception {
        return Result.success(empService.getById(id));
    }

    @PutMapping("/emps")
    public Result update(@RequestBody Emp emp) throws Exception {
        empService.update(emp);
        return Result.success();
    }
}
