package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.util.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@Api(tags = "医院设置管理")
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    HospitalSetService hospitalSetService;

    @ApiOperation("查询所有医院设置")
    @GetMapping("/findAll")
    public Result findAll(){
        List<HospitalSet> list = hospitalSetService.list();

        return Result.ok(list);
    }

    @ApiOperation("根据id删除医院设置")
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Long id){
        boolean b = hospitalSetService.removeById(id);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

    @ApiOperation("条件分页查询医院设置")
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> hospitalSetPage = new Page<>(current, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())){
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())){
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }
        Page<HospitalSet> page = hospitalSetService.page(hospitalSetPage, wrapper);
        return Result.ok(page);
    }

    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //todo 设置的校验
        //设置状态(1为可用，0为不可用)
        hospitalSet.setStatus(1);
        //生成md5签名
        Random random = new Random();
        String sign = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(sign);
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询医院设置")
    @GetMapping("/{id}")
    public Result findById(@PathVariable long id){
        HospitalSet set = hospitalSetService.getById(id);
        return Result.ok(set);
    }

    @ApiOperation("修改医院设置")
    @PostMapping("/update")
    public Result updateById(@RequestBody HospitalSet hospitalSet){
        //todo 设置的校验
        boolean b = hospitalSetService.updateById(hospitalSet);
        if (b){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/batchRemoveHospital")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> ids){
        boolean b = hospitalSetService.removeByIds(ids);
        return Result.ok();
    }


    @ApiOperation("医院设置锁定/解锁")
    @PostMapping("/lock/{id}/{status}")
    public Result lockHospitalSet(@PathVariable long id,
                                  @PathVariable Integer status){
        HospitalSet set = hospitalSetService.getById(id);
        if (set==null){
            //todo 抛出异常信息
        }
        set.setStatus(status);
        boolean b = hospitalSetService.updateById(set);
        return Result.ok();
    }

    @ApiOperation("发送医院的签名密钥")
    @PutMapping("/sendKsy/{id}/")
    public Result sendSign(@PathVariable long id){
        HospitalSet set = hospitalSetService.getById(id);
        if (set==null){
            //todo 抛出异常信息
        }
        String hoscode = set.getHoscode();
        String signKey = set.getSignKey();
        //todo 发送短信
        return  Result.ok();
    }







}
