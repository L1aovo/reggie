package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * @author L1ao
 * @version 1.0
 */
public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表
    public void saveWithFlavors(DishDto dishDto);

    public DishDto getByIdWithFlavors(Long id);

    public void updateWithFlavors(DishDto dishDto);
}
