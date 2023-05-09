package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;


/**
 * @author L1ao
 * @version 1.0
 */
public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
