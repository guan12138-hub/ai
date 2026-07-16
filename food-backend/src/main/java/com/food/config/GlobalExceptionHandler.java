package com.food.config;

import cn.dev33.satoken.exception.NotLoginException;
import com.food.util.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLogin(NotLoginException e) {
        return R.error(401, "登录已过期，请重新登录");
    }

    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntime(RuntimeException e) {
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        return R.error("服务器异常: " + e.getMessage());
    }
}
