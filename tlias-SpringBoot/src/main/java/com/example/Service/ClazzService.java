package com.example.Service;

import com.example.Entity.Clazz;
import com.example.Entity.ClazzPages;
import com.example.Entity.PageBean;

import java.time.LocalDate;
import java.util.List;

public interface ClazzService {
    public ClazzPages list(String name,LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    void delete(Integer id);

    void add(Clazz clazz);

    Clazz get(Integer id);

    void update(Clazz clazz);

    List<Clazz> list();
}
