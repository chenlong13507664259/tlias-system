package com.example.Service;

import com.example.Entity.Dept;

import java.util.List;

public interface DeptService
{
    public List<Dept> list() throws Exception;
    public void deleteById(Integer id) throws Exception;
    public void add(Dept dept) throws Exception;
    public void update(Dept dept) throws Exception;
    public Dept findById(Integer id) throws Exception;



}
