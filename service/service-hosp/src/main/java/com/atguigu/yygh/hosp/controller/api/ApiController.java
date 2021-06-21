package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Api(tags = "医院管理api接口")
@RestController
@RequestMapping("api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;


    @RequestMapping("/saveHospital")
    @ApiOperation("上传（新建/修改）医院")
    public Result saveHosp(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        hospitalService.save(map);
        return Result.ok();
    }


    @RequestMapping("/hospital/show")
    @ApiOperation("查询医院")
    public Result showHospital(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        Hospital hospital = hospitalService.showHospital(map);
        return Result.ok(hospital);
    }

    @RequestMapping("/saveDepartment")
    @ApiOperation("上传（新建/修改）医院")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        departmentService.saveDepartment(map);
        return Result.ok();
    }

    @RequestMapping("/department/list")
    @ApiOperation("查询科室列表")
    public Result listDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        Page<Department> departments = departmentService.listDepartment(map);
        return Result.ok(departments);
    }

    @RequestMapping("/department/remove")
    @ApiOperation("删除科室")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        departmentService.removeDepartment(map);
        return Result.ok();
    }


    @RequestMapping("/saveSchedule")
    @ApiOperation("上传排班")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        scheduleService.saveSchedule(map);
        return Result.ok();
    }

    @RequestMapping("/schedule/list")
    @ApiOperation("查询排班列表")
    public Result listSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        Page<Schedule> schedules = scheduleService.listSchedule(map);
        return Result.ok(schedules);
    }

    @RequestMapping("/schedule/remove")
    @ApiOperation("删除排班")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        scheduleService.removeSchedule(map);
        return Result.ok();
    }






}
