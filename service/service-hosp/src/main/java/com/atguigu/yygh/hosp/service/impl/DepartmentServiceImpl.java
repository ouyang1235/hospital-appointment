package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.common.exception.BusinessException;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.utils.CheckSign;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> map) {
        String s = JSONObject.toJSONString(map);
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
        //转换
        Department departmentDto = JSONObject.parseObject(s, Department.class);
        //如果存在则修改，不存在则添加
        Department department = departmentRepository.getDepartmentByDepcodeAndHoscode(departmentDto.getDepcode(),departmentDto.getHoscode());
        //不存在
        if (department ==null){
            departmentDto.setCreateTime(new Date());
            departmentDto.setUpdateTime(new Date());
            departmentDto.setIsDeleted(0);
        }else {
            departmentDto.setUpdateTime(new Date());
            departmentDto.setId(department.getId());
            departmentDto.setCreateTime(department.getCreateTime());
        }
        departmentRepository.save(departmentDto);

    }

    @Override
    public Page<Department> listDepartment(Map<String, Object> map) {
        String s = JSONObject.toJSONString(map);
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
        //构造分页查询信息
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 1 : Integer.parseInt((String) map.get("limit"));
        Department department = JSONObject.parseObject(s, Department.class);
        PageRequest pageRequest = PageRequest.of(page-1, limit);
        //创建example对象
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Department> example = Example.of(department, exampleMatcher);
        Page<Department> pageList = departmentRepository.findAll(example, pageRequest);

        return pageList;
    }

    @Override
    public void removeDepartment(Map<String, Object> map) {
        CheckSign.check(map,hospitalSetMapper);
        String hoscode = (String)map.get("hoscode");
        String depcode = (String)map.get("depcode");
        Department department = departmentRepository.getDepartmentByDepcodeAndHoscode(depcode,hoscode);
        if (department!=null){
            departmentRepository.delete(department);
        }
    }


}
