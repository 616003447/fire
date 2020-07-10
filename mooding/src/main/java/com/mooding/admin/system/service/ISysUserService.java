package com.mooding.admin.system.service;

import com.mooding.admin.common.config.model.ResponseResult;
import com.mooding.admin.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    public ResponseResult checkUserIsEffective(SysUser sysUser);


}
