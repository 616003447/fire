package com.mooding.admin.system.base;

import com.mooding.admin.system.model.LoginUserVo;

/**
 *  system 底层共通业务API，提供其他独立模块调用
 * @Author BlueFire
 * @Date 2020/7/11 -16:02
 */
public interface ISysBaseAPI {
    /**
     * 日志添加
     * @param LogContent 内容
     * @param logType 日志类型(0:操作日志;1:登录日志;2:定时任务)
     * @param operatetype 操作类型(1:添加;2:修改;3:删除;)
     */
    void addLog(String LogContent, Integer logType, Integer operatetype);
    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    public LoginUserVo getUserByName(String username);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    public LoginUserVo getUserById(String id);
}
