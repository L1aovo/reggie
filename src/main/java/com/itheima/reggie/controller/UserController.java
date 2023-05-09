package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author L1ao
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("用户登录:{}", map);

        // 获取手机号
        String phone = (String) map.get("phone");
        // 获取验证码
        String code = (String) map.get("code");
        // 获取session中的验证码
        String sessionCode = (String) session.getAttribute(phone);
        // 判断验证码是否正确
        if (sessionCode != null && sessionCode.equals(code)) {
            // 比对正确，登录成功
            // 判断手机号是否存在，如果不存在，自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);

            if (user == null) {
                // 注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }



        return R.error("登录失败");
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 发送短信
//        log.info("发送短信成功:{}", user.getPhone());

        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone) && phone.length() == 11) {
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("发送短信成功:{},验证码为:{}", phone, code);

            session.setAttribute(phone, code);
            return R.success("发送短信成功");
        }

        return R.error("发送短信失败");
    }
}
