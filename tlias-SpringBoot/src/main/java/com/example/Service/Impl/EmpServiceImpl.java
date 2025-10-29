package com.example.Service.Impl;

import com.example.Entity.*;
import com.example.Mapper.EmpExprMapper;
import com.example.Mapper.EmpMapper;
import com.example.Service.EmpLogService;
import com.example.Service.EmpService;
import com.example.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;

    @Override
    public PageBean page(Integer page, Integer pageSize, String name, String gender, String begin, String end) throws Exception {
        Long total = empMapper.count(name, gender, begin, end);
        Integer start = (page - 1) * pageSize;
        List<Emp> empList = empMapper.page(start, pageSize, name, gender, begin, end);
        return new PageBean(total, empList);


    }


    @Transactional //事务:里面的所有操作都成功,则commit；任何一个失败则rollback
    @Override
    public void add(Emp emp) {
        try {
            //1.保存员工基本信息到emp
            emp.setPassword("123456");
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            //2.保存员工工作经历信息到emp_expr
            List<EmpExpr> exprList = emp.getExprList();
            //2.1关联员工id
            if (!CollectionUtils.isEmpty(exprList)) {
                for (EmpExpr empExpr : exprList) {
                    empExpr.setEmpId(emp.getId());
                }
            }
            //2.2批量保存员工工作经历
            empExprMapper.insertBatch(exprList);
        } finally {
            //无论新增成功与否,都记录员工操作日志
            EmpLog empLog = new EmpLog(emp.getId(), LocalDateTime.now(), "新增员工信息 " + " 姓名： " + emp.getName() + " " + "员工编号：" + emp.getId() + "员工姓名：" + emp.getName() + "员工部门：" + emp.getDeptName() + "员工入职时间：" + emp.getEntryDate() + "员工职位：" + emp.getJob() + "员工手机：" + emp.getPhone() + "员工照片：" + emp.getImage() + "员工性别：" + emp.getGender() + "员工薪资：" + emp.getSalary());
            empLogService.insertLog(empLog);

        }
    }


    @Override
    @Transactional
    public void delete(List<Integer> ids) throws Exception {
        empMapper.deleteBatch(ids);
        empExprMapper.deleteBatch(ids);
    }

    @Override
    public Emp getById(Integer id) {
//        //法1.查询员工基本信息和工作经历信息
//        return empMapper.getById(id);
//    }

        //法2.1.查询员工基本信息
        Emp emp = empMapper.getById2(id);
        //法2.2.查询员工工作经历信息
        List<EmpExpr> exprList = empExprMapper.getByEmpId(id);
        emp.setExprList(exprList);
        return emp;
    }

    @Override
    @Transactional//事务:里面的所有操作都成功,则commit；任何一个失败则rollback
    public void update(Emp emp) {
        //1.更新员工基本信息
        //1.1.设置更新时间
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);


        //2.更新员工工作经历信息
        //2.1.删除员工原来的工作经历再增加新的工作经历
        empExprMapper.deleteByEmpId(emp.getId());
        List<EmpExpr> exprList = emp.getExprList();
        //2.2.关联员工id
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(expr -> {
                expr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 员工登录
     **/
    @Override
    public EmpLoginInfo login(Emp emp) {
       // 1.查询员工
        Emp empDB=empMapper.getByUsernameAndPassword(emp.getUsername(),emp.getPassword());
        // 2.判断用户名密码是否正确
        if (empDB!=null){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",empDB.getId());
            claims.put("username",empDB.getUsername());

            //调用工具类生成JWT
           String jwt = JwtUtils.generateJwt(claims);
            return new EmpLoginInfo(empDB.getId(),empDB.getUsername(),empDB.getName(),jwt);
        }
        return null;
    }
}
