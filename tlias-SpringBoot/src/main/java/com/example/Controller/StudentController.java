package com.example.Controller;

import com.example.Entity.Result;
import com.example.Entity.Student;
import com.example.Entity.StudentsPages;
import com.example.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    /**
     * 获取学生基本信息
     * @return
     */
    @GetMapping("/students")
    public Result list(@RequestParam String name, @RequestParam String degree,@RequestParam String clazzId,@RequestParam Integer page,@RequestParam Integer pageSize) {
        StudentsPages studentsPages = studentService.list(name, degree, clazzId, page, pageSize);
        return Result.success(studentsPages);
    }

    /**
     * 批量删除学生信息
     * @param ids=1，2，3
     * @return
     */
    @DeleteMapping("/students/{ids}")
    public Result delete(@PathVariable String ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择需要删除的记录");
        }
        String[] split = ids.split(",");
        for (String id : split) {
            studentService.delete(Integer.parseInt(id));
        }
        return Result.success();
    }


    /**
     * 添加学员信息
     * @param student
     * @return
     */
    @PostMapping("/students")
    public Result add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    /**
     * 根据id查询学员信息
     * @param id
     * @return
     */
    @GetMapping("/students/{id}")
    public Result get(@PathVariable Integer id) {
        Student student = studentService.get(id);
        return Result.success(student);
    }

    /**
     * 修改学员信息
     * @param student
     * @return
     */
    @PutMapping("/students")
    public Result update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }

    /**
     * 扣分score
     * @return
     */
    @PutMapping("/students/violation/{id}/{score}")
    public Result updateViolation(@PathVariable Integer id, @PathVariable Short score) {
        Student student = studentService.get(id);

        // 累加分数
        student.setViolationScore((short) (student.getViolationScore() + score));
        // 根据 score 的正负更新次数
        if (score > 0) {
            // 正数，次数加一
            student.setViolationCount((short) (student.getViolationCount() + 1));
        } else if (score < 0) {
            // 负数，次数减一
            student.setViolationCount((short) (student.getViolationCount() - 1));
        }

        // 更新学生信息
        studentService.update(student);
        return Result.success();
    }

}
