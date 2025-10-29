//package com.example;
//
//import com.example.Interceptor.interceptorDemo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///*
// * 拦截器
// */
//@Configuration//配置类
//@Slf4j
//public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    interceptorDemo interceptorDemo;
//
//    //添加拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("添加拦截器");
//        registry.addInterceptor(new interceptorDemo()).addPathPatterns("/**").excludePathPatterns("/login");
//    }
//}
