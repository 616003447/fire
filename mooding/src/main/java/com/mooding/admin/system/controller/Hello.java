package com.mooding.admin.system.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author BlueFire
 * @Date 2020/7/4 -11:28
 */
@RestController
@RequestMapping("/api")
public class Hello {
    @RequestMapping("/aaa")
    public String getHello() {
        return "hello world !";
    }
}
