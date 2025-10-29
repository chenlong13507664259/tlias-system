package com.example.Service.Impl;


import com.example.Entity.Student;
import com.example.Entity.StudentsPages;
import com.example.Mapper.StudentMapper;
import com.example.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public StudentsPages list(String name, String degree, String clazzId, Integer page, Integer pageSize) {
        Long total = studentMapper.count(name, degree, clazzId);
        List<Student> rows = studentMapper.studentPage(name, degree, clazzId, (page - 1) * pageSize, pageSize);
        return new StudentsPages(name, degree, clazzId, rows, total);
    }

    @Override
    public void delete(int id) {
        studentMapper.delete(id);
    }

    @Override
    public void add(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.add(student);
    }

    @Override
    public Student get(Integer id) {
        return studentMapper.get(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

}
