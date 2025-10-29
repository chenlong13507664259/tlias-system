package com.example.Service.Impl;

import com.example.Entity.EmpLog;
import com.example.Mapper.EmpLogMapper;
import com.example.Entity.EmpLog;
import com.example.Service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements com.example.Service.EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;

    /**
     * 添加员工操作日志
     * @param empLog
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)//开启新的事务，REQUIRES默认值，有则使用，没有则创建
    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
}
