package com.mooding.admin.system.controller;


import com.mooding.admin.common.config.model.ResponseResult;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author mooding 
 * @since 2020-07-04
 */
@RestController
@RequestMapping("/api/system/sysUser")
public class SysUserController {


    @Autowired
    private ISysUserService userService;
    @PostMapping("/getByUserName")
    public ResponseResult list(@RequestBody SysUser userDto)    {
        SysUser user = userService.selectUserByUserName(userDto.getUserName());
        return ResponseResult.okResult(user);
    }

    @RequestMapping("/")
    public String getHello() {
        return "hello world !";
    }
}

