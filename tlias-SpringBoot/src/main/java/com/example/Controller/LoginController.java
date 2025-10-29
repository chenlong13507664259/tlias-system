package com.example.Controller;

import com.example.Entity.Emp;
import com.example.Entity.EmpLoginInfo;
import com.example.Entity.Result;
import com.example.Service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private EmpService empService;

    /**
     * 员工登录
     * @param emp
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        EmpLoginInfo empLoginInfo = empService.login(emp);
        if (empLoginInfo == null){
            return Result.error("用户名或密码错误");
        }
        return Result.success(empLoginInfo);
    }
}
