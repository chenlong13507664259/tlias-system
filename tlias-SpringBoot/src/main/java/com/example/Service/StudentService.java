package com.example.Service;

import com.example.Entity.Student;
import com.example.Entity.StudentsPages;

import java.util.List;

public interface StudentService {

    StudentsPages list(String name, String degree, String clazzId, Integer page, Integer pageSize);

    void delete(int id);

    void add(Student student);

    Student get(Integer id);

    void update(Student student);


}
