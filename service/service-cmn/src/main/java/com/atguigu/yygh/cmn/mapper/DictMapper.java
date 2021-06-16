package com.atguigu.yygh.cmn.mapper;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    List<Long> getHasChildId (List<Long> idList);

}
