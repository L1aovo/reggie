package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author L1ao
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {

        orderService.submit(orders);
        return R.success("提交成功");
    }

    /**
     * 订单分页查询
     * page=1&pageSize=10&number=00&beginTime=2023-05-02%2000%3A00%3A00&endTime=2023-05-06%2023%3A59%3A59
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {
        // 分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        // 添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        // 分页查询
        Page<Orders> ordersPage = orderService.page(pageInfo, queryWrapper);

        return R.success(ordersPage);
    }

    /**
     * 修改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders) {
        log.info("修改订单：{}", orders.toString());
        // 查询订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId, orders.getId());
        Orders order = orderService.getOne(queryWrapper);

        // 更新状态
        order.setStatus(orders.getStatus());
        orderService.updateById(order);

        return R.success("修改订单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize, String number, String beginTime, String endTime) {
        // 分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        Long currentId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId,currentId);
        // 添加过滤条件
        // 添加排序条件
        // 分页查询
        Page<Orders> ordersPage = orderService.page(pageInfo, queryWrapper);


        return R.success(ordersPage);
    }
}
