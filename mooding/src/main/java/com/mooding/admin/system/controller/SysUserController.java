package com.mooding.admin.system.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mooding.admin.common.contants.SystemContans;
import com.mooding.admin.common.model.ResponseResult;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.service.ISysUserService;
import com.mooding.admin.utils.ObjectConvertUtils;
import com.mooding.admin.utils.security.PasswordUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
    @RequiresPermissions("")
    public ResponseResult list(@RequestBody SysUser userDto)    {
        SysUser user = userService.getOneUserByUser(userDto);
        return ResponseResult.okResult(user);
    }

    @RequestMapping("/")
    public String getHello() {
        return "hello world !";
    }

    public ResponseResult add(@RequestBody JSONObject jsonObject) {
        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setCreateTime(LocalDateTime.now());//设置创建时间
            String salt = ObjectConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUserName(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus("0");
            user.setDelFlag(SystemContans.USER_UNDEL_FLAG.toString());
            sysUserService.addUserWithRole(user, selectedRoles);
            sysUserService.addUserWithDepart(user, selectedDeparts);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return ResponseResult.okResult("");
    }
}

