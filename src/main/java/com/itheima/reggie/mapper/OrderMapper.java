package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author L1ao
 * @version 1.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}