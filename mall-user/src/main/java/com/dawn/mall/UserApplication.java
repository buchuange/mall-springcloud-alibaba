package com.dawn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Dawn
 * @Date: 2022/5/12 17:00
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.dawn.mall.mapper")
public class UserApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class, args);
    }
}
