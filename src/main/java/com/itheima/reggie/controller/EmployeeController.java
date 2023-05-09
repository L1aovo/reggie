package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author L1ao
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 1. 将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据页面提交的用户名username查询数据库中的用户信息
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. 如果没有查询到用户信息，返回登陆错误
        if (emp == null) {
            return R.error("登陆失败");
        }

        // 4. 密码比对，如果密码不一致，返回登陆错误
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败");
        }

        // 5. 查看员工状态，如果为禁用状态，返回员工已禁用
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 6. 登陆成功，将用户信息存入session中
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        // 初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        // 获取当前登陆用户
//        Long employeeId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(employeeId);
//        employee.setUpdateUser(employeeId);

        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("新增成功");
        }
        return null;
    }

    /**
     * 员工数据分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("员工数据分页查询，page:{},pageSize:{},name:{}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);//需要判断是否为空吗？
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工信息，员工信息：{}", employee.toString());

//        employee.setUpdateTime(LocalDateTime.now());
//        Long employeeId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(employeeId);

        boolean update = employeeService.updateById(employee);
        if (update) {
            return R.success("修改成功");
        }
        return null;
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息，id:{}", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("查询失败");
    }
}
