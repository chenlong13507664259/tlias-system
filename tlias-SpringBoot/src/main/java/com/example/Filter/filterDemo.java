//package com.example.Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//
//import java.io.IOException;
//
//@WebFilter("/*")//拦截所有请求(常用)
////@WebFilter("/emps/*")//拦截目录请求
////@WebFilter("/login")//拦截指定请求
//public class filterDemo implements Filter {
//
//    //初始化,web服务器启动时调用一次
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    //过滤请求,web服务器每次接收请求时调用
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        filterChain.doFilter(servletRequest,servletResponse);//放行
//    }
//
//    //销毁,web服务器停止时调用一次
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
