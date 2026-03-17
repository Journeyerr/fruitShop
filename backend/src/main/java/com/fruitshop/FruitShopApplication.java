package com.fruitshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 鲜果时光后台系统启动类
 */
@SpringBootApplication
@EnableAsync
@MapperScan("com.fruitshop.mapper")
public class FruitShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(FruitShopApplication.class, args);
        System.out.println("====================================");
        System.out.println("   鲜果时光后台系统启动成功！");
        System.out.println("====================================");
    }
}
