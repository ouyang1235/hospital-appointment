package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.aop.annotation.Check;
import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.hosp.utils.CheckSign;
import com.atguigu.yygh.model.hosp.Schedule;
import org.apache.xmlbeans.impl.piccolo.xml.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void saveSchedule(Map<String, Object> map) {
        CheckSign.check(map,hospitalSetMapper);
        String s = JSONObject.toJSONString(map);
        Schedule scheduleDto = JSONObject.parseObject(s, Schedule.class);
        //判断是否已存在来确定创建还是修改
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(scheduleDto.getHoscode(), scheduleDto.getHosScheduleId());
        if (schedule ==null){
            scheduleDto.setCreateTime(new Date());
            scheduleDto.setUpdateTime(new Date());
            scheduleDto.setIsDeleted(0);
        }else{
            scheduleDto.setUpdateTime(new Date());
            scheduleDto.setId(schedule.getId());
        }
        scheduleRepository.save(scheduleDto);
    }

    @Override
    public Page<Schedule> listSchedule(Map<String, Object> map) {
        CheckSign.check(map,hospitalSetMapper);
        String s = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(s, Schedule.class);
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 1 : Integer.parseInt((String) map.get("limit"));
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<Schedule> hoscode = scheduleRepository.findScheduleByHoscode((String) map.get("hoscode"), pageRequest);
        return hoscode;
    }

    @Check
    @Override
    public void removeSchedule(Map<String, Object> map) {
//        CheckSign.check(map,hospitalSetMapper);
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule!=null){
            scheduleRepository.delete(schedule);
        }
    }

}
