package com.atguigu.yygh.hosp.aop.aspect;

import com.atguigu.yygh.common.exception.BusinessException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class CheckSignAspect {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;


    @Pointcut(value = "@annotation(com.atguigu.yygh.hosp.aop.annotation.Check)")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void doBefore(JoinPoint jp){
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = jp.getArgs();
        int n = 0;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("map")){
                n = i;
            }
        }
        Map<String, Object> map = (Map<String, Object>) (args[n]);
        //校验md5加密后的sign值
        String sign = (String)map.get("sign");
        String hoscode = (String)map.get("hoscode");
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet set = hospitalSetMapper.selectOne(wrapper);
        String signKey = set.getSignKey();
        String signMd5 = MD5.encrypt(signKey);
        if (!signMd5.equals(sign)){
            throw new BusinessException(ResultCodeEnum.SIGN_ERROR);
        }
    }



}
