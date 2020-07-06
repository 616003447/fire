package com.mooding.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author BlueFire
 * @Date 2020/7/4 -10:24
 */
@SpringBootApplication
public class MoodingJarApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoodingJarApplication.class, args);
    }
}
