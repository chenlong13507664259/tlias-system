package com.example.Service.Impl;

import com.example.Entity.Clazz;
import com.example.Entity.ClazzPages;
import com.example.Entity.PageBean;
import com.example.Mapper.ClazzMapper;
import com.example.Service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;

    @Override
    public ClazzPages list(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {

        Long total = clazzMapper.count(name, begin, end);
        List<Clazz> rows = clazzMapper.ClazzPage(name, begin, end, (page - 1) * pageSize, pageSize);
        return new ClazzPages(name, begin, end, total, rows);
    }

    @Override
    public void delete(Integer id) {
        clazzMapper.delete(id);
    }

    @Override
    public void add(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.add(clazz);
    }

    @Override
    public Clazz get(Integer id) {
        return clazzMapper.get(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> list() {
        return clazzMapper.list();
    }


}

