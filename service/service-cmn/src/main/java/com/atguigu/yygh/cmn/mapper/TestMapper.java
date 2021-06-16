package com.atguigu.yygh.cmn.mapper;

import com.atguigu.yygh.cmn.TestPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface TestMapper extends BaseMapper<TestPO> {

    void batchInsert(List<TestPO> list);

}
