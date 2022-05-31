package com.dawn.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:29
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.dawn.mall.mapper")
public class PaymentApplication {

    public static void main(String[] args) {

        SpringApplication.run(PaymentApplication.class, args);
    }
}
