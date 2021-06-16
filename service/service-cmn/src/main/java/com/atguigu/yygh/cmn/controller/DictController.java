package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@Api(tags = "数据字典管理")
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;



    @GetMapping("findChildData/{id}")
    @ApiOperation("根据id查询子节点")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @GetMapping("exportExcel")
    @ApiOperation("导出数据字典")
    public void exportExcel(HttpServletResponse httpServletResponse){
        dictService.exportExcelData(httpServletResponse);
//        return Result.ok();
    }

    @PostMapping("importExcel")
    @ApiOperation("导入数据字典")
    public Result importExcelData(MultipartFile file){
        dictService.importExcelData(file);
        return Result.ok();
    }



}
