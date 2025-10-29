package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@MapperScan("com.example.Mapper")
@SpringBootApplication
public class SpringBoot02TliasWebManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot02TliasWebManagerApplication.class, args);
    }

}
