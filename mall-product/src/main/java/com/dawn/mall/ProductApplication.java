package com.dawn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 02:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.dawn.mall.mapper")
public class ProductApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProductApplication.class, args);
    }
}
