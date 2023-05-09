package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author L1ao
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前进行检测
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联了菜品，如果有就抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // 查询
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        // 查询当前分类是否关联了套餐，如果有就抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        // 查询
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        // 删除当前分类
        super.removeById(id);
    }
}
