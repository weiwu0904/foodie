package com.weiwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.weiwu.mapper")
@ComponentScan(basePackages = {"com.weiwu","org.n3r.idworker"})
public class FoodieApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodieApplication.class, args);
    }
}
