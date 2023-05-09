package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author L1ao
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {


    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车:{}", shoppingCart);
        // 设置用户id，从session中获取，当前用户
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        // 如果是套餐，套餐id不为空
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        // 如果购物车中已经存在该商品，数量加一
        if (cartServiceOne != null) {
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 减少购物车商品
     * @param shoppingCart
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        log.info("减少购物车商品 dishId:{},setmealId:{}", dishId, setmealId);
        // 获取当前用户
        Long currentId = BaseContext.getCurrentId();

        // 查询购物车中的商品
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer cartServiceOneNumber1 = cartServiceOne.getNumber();

        // 如果购物车中该商品数量大于一，数量减一
        if (cartServiceOneNumber1 != 1) {
            Integer cartServiceOneNumber = cartServiceOne.getNumber();
            cartServiceOne.setNumber(cartServiceOneNumber-1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCartService.removeById(cartServiceOne.getId());
        }

        return R.success("ok");
    }

    /**
     * 查看购物车中的商品
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车中的商品");
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        log.info("清空购物车");
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
