package com.example.Service.Impl;


import com.example.Entity.Dept;
import com.example.Mapper.DeptMapper;
import com.example.Service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    public List<Dept> list() throws Exception {
        return deptMapper.list();
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) throws Exception {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.add(dept);
    }

    @Override
    public void update(Dept dept) throws Exception {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

    @Override
    public Dept findById(Integer id) throws Exception {
        Dept dept = deptMapper.findById(id);
        return dept;
    }


}
