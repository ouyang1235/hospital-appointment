package com.atguigu.yygh.hosp.utils;

import com.atguigu.yygh.common.exception.BusinessException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CheckSign {


    public static void check(Map<String, Object> paramsMap, HospitalSetMapper hospitalSetMapper){
        //校验md5加密后的sign值
        String sign = (String)paramsMap.get("sign");
        String hoscode = (String)paramsMap.get("hoscode");
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
