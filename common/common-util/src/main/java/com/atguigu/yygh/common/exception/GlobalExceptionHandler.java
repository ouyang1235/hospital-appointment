package com.atguigu.yygh.common.exception;


import com.atguigu.yygh.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result handlerBusiness(BusinessException exception){
        log.info(exception.getMessage(),exception);
        return Result.build(exception.getCode(),exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception exception){
        log.info(exception.getMessage(),exception);
        return Result.build(500,"系统异常！请联系管理员");
    }

}
