package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author L1ao
 * @version 1.0
 * 全局异常处理
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("SQLIntegrityConstraintViolationException:{}", e.getMessage());

        if (e.getMessage().contains("Duplicate entry")) {
            String[] split = e.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("数据操作异常");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> handleCustomException(CustomException e) {
        log.error("CustomException:{}", e.getMessage());
        return R.error(e.getMessage());
    }
}
