package com.example.Service;

import com.example.Entity.Emp;
import com.example.Entity.EmpLoginInfo;
import com.example.Entity.PageBean;

import java.util.List;

public interface EmpService {
    public PageBean page(Integer page, Integer pageSize, String name, String gender, String begin, String end) throws Exception;

    void add(Emp emp);

    void delete(List<Integer> ids) throws Exception;

    Emp getById(Integer id);

    void update(Emp emp);

    EmpLoginInfo login(Emp emp);
}
