package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理sql异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //添加相同员工
        //报错：Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage(); //获得报错信息
        if (message.contains("Duplicate entry")){
            String[] split = message.split(" "); //对报错信息分割
            String username = split[2]; //信息中第三个字符串就是重复的用户名
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
