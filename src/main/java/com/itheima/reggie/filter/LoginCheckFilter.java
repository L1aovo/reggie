package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author L1ao
 * @version 1.0
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. 获取请求的uri
        String requestURI = request.getRequestURI();
        log.info("请求的uri:{}", requestURI);

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        String[] urlsEmployee = new String[]{
                "/employee/**",
                "/backend/**"
        };

        // 2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        // 3. 如果不需要处理，直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 4-1. 判断登陆状态，如果已经登陆，放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已经登陆，用户id为{}", request.getSession().getAttribute("employee"));
            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request, response);
            return;
        }

        // 4-2. 判断登陆状态，如果已经登陆，放行
        if (request.getSession().getAttribute("user") != null) {

            check = check(urlsEmployee, requestURI);
            // 判断是否是访问后台
            if (check) {
                log.info("客户不能访问后台", requestURI);
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                return;
            }


            log.info("用户已经登陆，用户id为{}", request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }


        // 5. 如果没有登陆，重定向到登陆页面
        log.info("用户未登陆");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
