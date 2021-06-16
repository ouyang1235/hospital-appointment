package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictExcelListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.exception.BusinessException;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;


    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> list = dictMapper.selectList(wrapper);
        if (!list.isEmpty()){
            List<Long> idList = list.stream().map(Dict::getId).collect(Collectors.toList());
            List<Long> hasChildId = dictMapper.getHasChildId(idList);
            for (Dict dict : list) {
                dict.setHasChildren(hasChildId.contains(dict.getId()));
            }
        }
        return list;
    }

    @Override
    public void exportExcelData(HttpServletResponse response) {
        //设置导出信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName;
        try{
            fileName = URLEncoder.encode("数据字典","UTF-8");
        }catch (Exception e){
            log.error("转码异常信息",e);
            throw new BusinessException("转码出现异常！");
        }
        response.setHeader("Content-disposition","attachment;filename="+fileName + ".xlsx");

        List<Dict> list = dictMapper.selectList(null);
        List<DictEeVo> listVo = list.stream().map(item -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(item, dictEeVo);
            return dictEeVo;
        }).collect(Collectors.toList());
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet(1).doWrite(listVo);
        } catch (IOException e) {
            log.error("导出路径异常信息",e);
            throw new BusinessException("导出出现异常！");
        }


    }

    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importExcelData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictExcelListener(this.dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
