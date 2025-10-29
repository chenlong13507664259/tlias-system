package com.example.Filter;


import com.example.Utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 登录检查过滤器
 */

@WebFilter("/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取请求路径
        String path = request.getRequestURI();
        System.out.println("path:"+path);
        //2.判断路径是否是登录,登录则放行
        if (path.contains("login")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;//结束当前请求
        }
        //3.获取请求头中的token
        String token = request.getHeader("token");
        System.out.println("token:"+token);
        //4.判断token是否为空,为空则拦截,响应401
        if (token==null || token.equals("")){
            System.out.println("token为空");
            response.setStatus(401);
            throw new RuntimeException("未登录");
        }
        //5.解析 token,解析失败则拦截,响应401
        try {
            Claims claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            System.out.println("解析失败");
            response.setStatus(401);
            throw new RuntimeException("未登录");
        }
        //6.解析成功则放行
        System.out.println("解析成功");
        filterChain.doFilter(servletRequest,servletResponse);
    }


}
