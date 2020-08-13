package com.mooding.admin.system.service;

import com.mooding.admin.common.model.ResponseResult;
import com.mooding.admin.system.entity.SysMenu;
import com.mooding.admin.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author mooding 
 * @since 2020-07-04
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 通过用户对象包含用户名电话号码，确定唯一用户
     *
     * @param SysUser 用户对象
     * @return 用户对象信息
     */
    public SysUser getOneUserByUser(SysUser user);

    /**
     * 通过用户id 查询用户
     *
     * @param userId 用户id
     * @return 用户对象信息
     */
    public SysUser getUserById(Long userId);

    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    public ResponseResult checkUserIsEffective(SysUser sysUser);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String username);
    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<SysMenu> getUserMenuSet(String username);
}
