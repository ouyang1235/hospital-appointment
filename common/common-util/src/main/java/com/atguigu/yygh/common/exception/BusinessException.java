package com.atguigu.yygh.common.exception;


import com.atguigu.yygh.common.result.ResultCodeEnum;
import lombok.Data;


/**
 * 全局异常类
 */
@Data
public class BusinessException extends RuntimeException{
    private Integer code;

    private String message;


    public BusinessException(Integer code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message){
        super(message);
        this.code = ResultCodeEnum.FAIL.getCode();
        this.message = message;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }


}
