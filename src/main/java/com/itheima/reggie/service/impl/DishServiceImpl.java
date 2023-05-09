package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author L1ao
 * @version 1.0
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表
     *
     * @param dishDto
     */

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : dishFlavors) {
            dishFlavor.setDishId(dishId);
        }
        // dishFlavors.stream().map((item) -> {item.setDishId(dishId);return item;}).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }

    @Override
    public DishDto getByIdWithFlavors(Long id) {
        // 查询菜品
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 查询菜品对应的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavors(DishDto dishDto) {
        // 更新dish表基本消息
        this.updateById(dishDto);
        // 清理dish_flavor表中的数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        // 重新插入dish_flavor表中的数据，即删了再加
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : dishFlavors) {
            dishFlavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(dishFlavors);
    }
}
