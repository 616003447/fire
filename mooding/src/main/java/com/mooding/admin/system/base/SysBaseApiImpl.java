package com.mooding.admin.system.base;

import com.mooding.admin.common.contants.SystemContans;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.model.LoginUserVo;
import com.mooding.admin.system.service.ISysUserService;
import com.mooding.admin.utils.ObjectConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 底层共通业务API，提供其他独立模块调用
 *
 * @Author BlueFire
 * @Date 2020/7/11 -16:03
 */
@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {

    @Autowired
    private ISysUserService userService;
    @Override
    public void addLog(String LogContent, Integer logType, Integer operatetype) {
        /*SysLog sysLog = new SysLog();
        //注解上的描述,操作日志内容
        sysLog.setLogContent(LogContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);

        //请求的方法名
        //请求的参数

        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }

        //获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(sysUser!=null){
            sysLog.setUserid(sysUser.getUsername());
            sysLog.setUsername(sysUser.getRealname());

        }
        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysLogMapper.insert(sysLog);*/
    }

    @Override
    @Cacheable(cacheNames= SystemContans.SYS_USERS_CACHE, key="#username")
    public LoginUserVo getUserByName(String username) {
        if(ObjectConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUserVo loginUser = new LoginUserVo();
        SysUser sysUser = userService.getOneUserByUser(new SysUser().setUserName(username));
        if(sysUser==null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public LoginUserVo getUserById(String id) {
        if(ObjectConvertUtils.isEmpty(id)) {
            return null;
        }
        LoginUserVo loginUser = new LoginUserVo();
        SysUser sysUser = userService.getUserById(Long.parseLong(id));
        if(sysUser==null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }
}
