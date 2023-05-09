package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author L1ao
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时插入套餐对应的菜品数据，需要操作两张表
     * @param setmealDto
     */
    public void saveWithDishes(SetmealDto setmealDto);

    public void removeWithDishes(List<Long> ids);
}
