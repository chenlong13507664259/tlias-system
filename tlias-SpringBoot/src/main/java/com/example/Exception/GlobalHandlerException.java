package com.example.Exception;

import com.example.Entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice//用来捕获Controller中的所有异常
public class GlobalHandlerException {
    @ExceptionHandler
    public Result doException(Exception e)
    {
        log.error(e.getMessage());
        return Result.error("出错了，请联系管理员！");
    }
}
