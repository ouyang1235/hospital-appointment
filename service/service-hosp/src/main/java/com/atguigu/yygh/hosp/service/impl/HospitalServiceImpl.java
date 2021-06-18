package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.BusinessException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl  implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Override
    public void save(Map<String, Object> map) {
        //转换
        String s = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(s, Hospital.class);
        //校验md5加密后的sign值
        String sign = (String)map.get("sign");
        String hoscode = hospital.getHoscode();
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet set = hospitalSetMapper.selectOne(wrapper);
        String signKey = set.getSignKey();
        String signMd5 = MD5.encrypt(signKey);
        if (!signMd5.equals(sign)){
            throw new BusinessException(ResultCodeEnum.SIGN_ERROR);
        }
        //base64加密的图片处理
        String beforeLogo = hospital.getLogoData();
        String afterLogo = beforeLogo.replace(" ", "+");
        hospital.setLogoData(afterLogo);
        //校验是否存在，不存在就添加，存在就修改
        Hospital exist = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if (exist==null){
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setStatus(0);
            hospital.setIsDeleted(0);
            Hospital insert = hospitalRepository.save(hospital);
        }else {
            hospital.setUpdateTime(new Date());
            hospital.setStatus(0);
            hospital.setIsDeleted(0);
            hospital.setId(exist.getId());
            hospital.setCreateTime(exist.getCreateTime());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital showHospital(Map<String, Object> map) {
        //转换
        String s = JSONObject.toJSONString(map);
//        Hospital hospital = JSONObject.parseObject(s, Hospital.class);
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
        //根据hoscode查询信息
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }
}
